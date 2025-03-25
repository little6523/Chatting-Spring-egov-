package chat;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatServer extends WebSocketServer {

    private static final int PORT = 12345;  // 서버 포트 번호
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private List<User> users;

    public static void main(String[] args) {
        ChatServer server = new ChatServer(PORT);
        server.start();
        System.out.println("채팅 서버가 실행 중입니다...");
    }

    public ChatServer(int port) {
        super(new InetSocketAddress(SERVER_ADDRESS, port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	InetSocketAddress isa = (InetSocketAddress) conn.getRemoteSocketAddress();
        System.out.println("새 클라이언트 연결됨: " + isa.getAddress().getHostAddress() + ":" + isa.getPort());
        Map<String, Object> map = new HashMap<>();
        map.put("message", "서버에 연결되었습니다!");
        try {
			sendToOne(conn, map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("클라이언트 연결 종료: " + conn.getRemoteSocketAddress());
        for (User user : users) {
            if (user.getWebSocket() == conn) {
                users.remove(user);
                break;
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, String data) {
    	
        try {
            // 메시지를 JSON 객체로 변환
            Map<String, Object> map = new HashMap<>();
            map = new ObjectMapper().readValue(data, Map.class);
            System.out.println("클라이언트 정보: " + map);
            
            if (map.containsKey("name")) {
            	String name = (String) map.get("name");
            	InetSocketAddress isa = (InetSocketAddress) conn.getRemoteSocketAddress();
                User newUser = new User(name, isa.getAddress().getHostAddress(), isa.getPort(), conn);
                users.add(newUser);
                
                Map<String, Object> participation = new HashMap<>();
                participation.put("userNumber", users.size());
                
                List<String> userNames = new ArrayList<>();
                for (User user : users) {
                	userNames.add(user.getName());
                }
                participation.put("userNames", userNames);
                sendToAll(participation);
                return;
            }
            
            if (map.containsKey("message")) {
                System.out.println("받은 메시지: " + map.get("message"));
                for (User user : users) {
                    // 현재 클라이언트에게는 메시지를 전송하지 않음
                    if (user.getWebSocket() != conn) {
                    	Map<String, Object> message = new HashMap<>();
                    	map.put("name", user.getName());
                    	map.put("name", map.get("message"));
                    	sendToOne(user.getWebSocket(), map);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn.send("잘못된 형식의 메시지입니다.");
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("서버 시작됨...");
        users = new ArrayList<>();
    }
    
    private void sendToOne(WebSocket conn, Map<String, Object> message) throws JsonProcessingException {
    	String jsonMessage = mapToJson(message);
    	conn.send(jsonMessage);
	}
    
    private void sendToAll(Map<String, Object> message) throws JsonProcessingException {
		for (User user : users) {
			String jsonMessage = mapToJson(message);
			user.getWebSocket().send(jsonMessage);
		}
	}
    
    private Map<String, Object> jsonToMap(String json) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, Map.class);
	}
    
    private String mapToJson(Map<String, Object> map) throws JsonProcessingException {
    	ObjectMapper objectMapper = new ObjectMapper();
    	return objectMapper.writeValueAsString(map);
	}
}
