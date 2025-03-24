package chat;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatServer extends WebSocketServer {

    private static final int PORT = 12345;  // 서버 포트 번호
    private List<User> users;

    public static void main(String[] args) {
        ChatServer server = new ChatServer(PORT);
        server.start();
        System.out.println("채팅 서버가 실행 중입니다...");
    }

    public ChatServer(int port) {
        super(new InetSocketAddress("127.0.0.1", port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    	InetSocketAddress isa = (InetSocketAddress) conn.getRemoteSocketAddress();
        System.out.println("새 클라이언트 연결됨: " + isa.getAddress().getHostAddress() + ":" + isa.getPort());
        conn.send("서버에 연결되었습니다!");  // 새로 연결된 클라이언트에게 메시지 전송
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("클라이언트 연결 종료: " + conn.getRemoteSocketAddress());
        for (User user : users) {
            // 현재 클라이언트에게는 메시지를 전송하지 않음
            if (user.getWebSocket() == conn) {
                users.remove(user);
                break;
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
    	
        try {
            // 메시지를 JSON 객체로 변환
            Map<String, Object> userInfo = new HashMap<>();
            userInfo = new ObjectMapper().readValue(message, Map.class);
            System.out.println("클라이언트 정보: " + userInfo);
            
            String connect = (String) userInfo.get("connect");
            if (connect == null) {
            	return;
            }
            
            if (connect.equals("1")) {
            	String name = (String) userInfo.get("name");
            	InetSocketAddress isa = (InetSocketAddress) conn.getRemoteSocketAddress();
                System.out.println("새 클라이언트 연결됨: " + name + " | " + isa.getAddress().getHostAddress() + ":" + isa.getPort());
                User user = new User(name, isa.getAddress().getHostAddress(), isa.getPort(), conn);
                users.add(user);
            } else {
                System.out.println("받은 메시지: " + userInfo.get("message"));
                for (User user : users) {
                    // 현재 클라이언트에게는 메시지를 전송하지 않음
                    if (user.getWebSocket() != conn) {
                        user.getWebSocket().send(userInfo.get("name") + ": " + (String) userInfo.get("message"));
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
}
