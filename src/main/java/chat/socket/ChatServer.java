package chat.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint("/chat")
public class ChatServer {

    private static ChatService2 chatService2 = new ChatService2();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        String clientAddress = session.getRequestURI().getHost();
        int clientPort = session.getRequestURI().getPort();
        System.out.println("새 클라이언트 연결됨: " + clientAddress + ":" + clientPort);

        // 클라이언트가 연결되었을 때 메시지 전송
        Map<String, Object> map = new HashMap<>();
        map.put("name", "서버");
        map.put("message", "서버에 연결되었습니다!");
        sendMessage(session, map);

        // 서비스 초기화
        chatService2.init();
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("클라이언트 연결 종료: " + session.getId());
        Map<String, Object> participation = chatService2.removeUser(session);
        sendToAll(participation);
    }

    @OnMessage
    public void onMessage(Session session, String data) {
        try {
            Map<String, Object> dataMap = jsonToMap(data);

            if (dataMap.containsKey("name")) {
                Map<String, Object> participation = chatService2.newClient(dataMap, session);
                sendToAll(participation);
                return;
            }

            if (dataMap.containsKey("message")) {
                for (User user : chatService2.getUsers()) {
                    if (user.getSession() != session) {
                        dataMap.put("name", user.getName());
                        sendMessage(user.getSession(), dataMap);
                    }
                }
                return;
            }

            if (dataMap.containsKey("room")) {
                // 방을 개설하는 로직
//                User manager = new User("철수", "127.0.0.1", 7777);
//                chatService2.createRoom("심심해서 만든 방", manager);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.getBasicRemote().sendText("잘못된 형식의 메시지입니다.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // 특정 유저에게 메시지 전송
    private void sendMessage(Session session, Map<String, Object> message) throws IOException {
        String jsonMessage = mapToJson(message);
        session.getBasicRemote().sendText(jsonMessage);
    }

    // 모든 유저에게 메시지 전송
    private void sendToAll(Map<String, Object> message) {
        for (User user : chatService2.getUsers()) {
            try {
                sendMessage(user.getSession(), message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Object> jsonToMap(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private String mapToJson(Map<String, Object> map) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
