package chat;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatServer extends WebSocketServer {

    private static final String SERVER_ADDRESS = "127.0.0.1"; // 서버 IP 주소
    private static final int PORT = 12345;  // 서버 포트 번호
    
    // => 일반 List를 사용하지 않은 이유: 향상된 for문으로 돌려서 요소를 제거하면 ConcurrentModificationException 오류 발생
    // => 해결방법: 1. Iterator를 통해 요소 추가 및 삭제 / 2. CopyOnWriteArrayList 활용
    // private CopyOnWriteArrayList<User> users; // 접속 중인 유저리스트
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
        Map<String, Object> participation = new HashMap<>();
        List<String> userNames = new ArrayList<>();
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
        	User u = iterator.next();
            if (u.getWebSocket() != conn) {
            	userNames.add(u.getName());
            	continue;
            }
            iterator.remove();
        }
        
        participation.put("userNumber", users.size());
        participation.put("userNames", userNames);
        try {
			sendToAll(participation);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void onMessage(WebSocket conn, String data) {
    	
        try {
            Map<String, Object> map = new HashMap<>();
            map = jsonToMap(data);
            
            if (map.containsKey("name")) {
                System.out.println("클라이언트 이름: " + map);
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
                    	map.put("name", user.getName());
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
//        users = new CopyOnWriteArrayList<>();
        users = new ArrayList<>();
    }
    
    // 특정 유저에게 메시지 전송
    private void sendToOne(WebSocket conn, Map<String, Object> message) throws JsonProcessingException {
    	String jsonMessage = mapToJson(message);
    	conn.send(jsonMessage);
	}
    
    // 모든 유저에게 메시지 전송
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
