package chat.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import annotation.chat;

@chat
public class ChattingRoomManager {
	
	private Map<String, Room> rooms = new HashMap<>();

	public Room getChattingRoom(String roomName) {
		return rooms.get("roomName");
	}
	
	public int createRoom(String roomName, Room room) {
		rooms.put(roomName, room);
		return rooms.size();
	}
	
    // 새로 연결된 클라이언트가 있는 경우
    public List<User> newClient(Map<String, Object> userInfo, String roomName, Session session) {
        System.out.println("클라이언트 이름: " + userInfo.get("name"));
        String name = (String) userInfo.get("name");
        User newUser = new User(name, session);
        
        System.out.println("rooms: " + rooms);
        
        Room room  = rooms.get(roomName);
        room.addUser(newUser);

        return room.getParticipatns();
    }
    
    // 연결 종료된 클라이언트가 있는 경우
    public Map<String, Object> removeUser(Session session, String roomName) {
        Map<String, Object> participation = new HashMap<>();
        List<String> userNames = new ArrayList<>();
        List<User> users = rooms.get(roomName).getParticipatns();
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User u = iterator.next();
            if (u.getSession() != session) {
                userNames.add(u.getName());
                continue;
            }
            iterator.remove();
        }
        participation.put("userNumber", users.size());
        participation.put("userNames", userNames);
        return participation;
    }
}
