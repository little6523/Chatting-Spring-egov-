package chat.api.service;

import java.util.Map;

import chat.socket.User;

public interface ChatApiService {
	
	public Map<String, Object> login(Map<String, Object> body);
	
	public String getImage(Map<String, Object> body);
	
	public void postImage(String image, String oldNickname, String newNickname);

	public int createChattingRoom(Map<String, Object> body);

	public void enterChattingRoom(Map<String, Object> body);

	public void exitChattingRoom(Map<String, Object> body);

	public void changeNickname(String oldNickname, String newNickname);

	public void changePassword(String oldNickname, String password);
}
