package chat.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chat.annotation.chat;
import chat.log.ChattingFileManager;
import chat.socket.config.ChatServerConfig;

@ServerEndpoint(value = "/chat/{roomName}", configurator = ChatServerConfig.class)
@chat
public class ChatServer {
	
	@Autowired
	private ChattingRoomManager chattingRoomManager;
	
	@Autowired
	private ChattingFileManager chattingFileManager;

    @OnOpen
    public void onOpen(@PathParam("roomName") String roomName, Session session) throws IOException {
        String clientAddress = session.getRequestURI().getHost();
        int clientPort = session.getRequestURI().getPort();
        System.out.println("새 클라이언트 연결됨: " + clientAddress + ":" + clientPort);

        // 클라이언트가 연결되었을 때 메시지 전송
        Map<String, Object> map = new HashMap<>();
        map.put("name", "서버");
        map.put("message", "서버에 연결되었습니다!");
        
        sendMessage(session, map);
        
        String oldChatting = chattingFileManager.readChatting(roomName);
        if (oldChatting == null || oldChatting.equals("")) {
        	return;
        }
        
        try (BufferedReader reader = new BufferedReader(new StringReader(oldChatting))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	String arr[] = line.split(":");
                map.put("name", arr[0]);
                map.put("message", arr[1]);
                sendMessage(session, map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("클라이언트 연결 종료: " + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String data) {
        try {
            Map<String, Object> message = jsonToMap(data);
            String roomName = (String) message.get("roomName");
            Room room = chattingRoomManager.getChattingRoom(roomName);

            if (message.containsKey("name")) {
                List<User> users = chattingRoomManager.newClient(message, roomName, session);
                Map<String, Object> participants = new HashMap<>();
                participants.put("users", users);
                sendToAll(users, participants);
                return;
            }

            if (message.containsKey("message")) {
            	message.put("name", message.get("userName"));
            	chattingFileManager.saveChatting(roomName, message.get("userName") + ":" + message.get("message"));
                for (User user : room.getParticipatns()) {
                    if (user.getSession() != session) {
                        sendMessage(user.getSession(), message);
                    }
                }
                return;
            }
            
            if (message.containsKey("close")) {
            	List<User> users = chattingRoomManager.removeUser(session, roomName);
                Map<String, Object> participants = new HashMap<>();
                participants.put("users", users);
                sendToAll(users, participants);
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
    private void sendToAll(List<User> users, Map<String, Object> message) {
        for (User user : users) {
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
