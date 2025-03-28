package chat.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.springframework.stereotype.Component;

@Component
public class ChatService2 {

    // 접속 중인 유저 리스트
    private List<User> users;
    private List<Room> rooms;

    public void init() {
        users = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
    
    // 새로 연결된 클라이언트가 있는 경우
    public Map<String, Object> newClient(Map<String, Object> dataMap, Session session) {
        System.out.println("클라이언트 이름: " + dataMap.get("name"));
        String name = (String) dataMap.get("name");
        User newUser = new User(name, session);
        users.add(newUser);

        Map<String, Object> participation = new HashMap<>();
        participation.put("userNumber", users.size());

        List<String> userNames = new ArrayList<>();
        for (User user : users) {
            userNames.add(user.getName());
        }
        participation.put("userNames", userNames);
        return participation;
    }

    public Map<String, Object> removeUser(Session session) {
        Map<String, Object> participation = new HashMap<>();
        List<String> userNames = new ArrayList<>();
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

    public int createRoom(String roomName, User manager) {
        Room room = new Room(roomName, manager.getName());
        room.addUser(manager);
        rooms.add(room);

        return rooms.size();
    }

    public void enterRoom(User user, int roomId) {
        rooms.get(roomId).addUser(user);
    }
}
