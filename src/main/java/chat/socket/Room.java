package chat.socket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Room {
	
	private int roomId;
	
	private String roomName;
	
	private String manager;
	
	private List<User> participants;
	
	private List<String> conversations;
	
	public Room(String roomName, String manager) {
		this.roomName = roomName;
		this.manager = manager;
		this.participants = new ArrayList<>();
	}
	
	public String getRoomName() {
		return this.roomName;
	}
	
	public String getManager() {
		return this.manager;
	}
	
	public List<User> getParticipatns() {
		return this.participants;
	}
	
	public List<String> getConversation() {
		return this.conversations;
	}
	
	public void addUser(User user) {
		this.participants.add(user);
	}
	
	public void removeUser(User user) {
        Iterator<User> iterator = this.participants.iterator();
        while (iterator.hasNext()) {
        	User u = iterator.next();
            if (u.getSession() == user.getSession()) {
            	iterator.remove();
            }
        }
	}
}
