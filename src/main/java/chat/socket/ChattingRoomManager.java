package chat.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.Session;

import annotation.chat;
import chat.webview.mapper.ChatMapper;

@chat
public class ChattingRoomManager {
	
	private Map<String, Room> rooms = new HashMap<>();
	
	@Resource(name = "ChatMapper")
	private ChatMapper chatMapper;
	
	// PostConstruct를 사용한 이유는 채팅방의 경우, DB에 존재하는 채팅방 데이터를 미리 불러와야 하기 때문
	// 채팅방 초기화 로직은 채팅방을 불러올 때 기존의 채팅하던 방이 있으면 그 채팅방도 같이 초기화되 때문에
	// 서버가 한 번 시작할 때만 초기화하도록 변경
	@PostConstruct
	public void initChattingRoom() {
		List<Map<String, Object>> map = chatMapper.getChattingRooms();
		Map<String, Room> rooms = new HashMap<>();
		for(Map<String, Object> m : map) {
			Room room = new Room((String) m.get("name"), (String) m.get("manager"));
			rooms.put((String) m.get("name"), room);
		}
		this.rooms = rooms;
	}

	public Room getChattingRoom(String roomName) {
		return rooms.get(roomName);
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
