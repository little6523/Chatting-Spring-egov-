package chat.api.service;

import java.util.Map;

import chat.socket.User;

public interface ChatApiService {

	public int createRoom(Map<String, Object> body);

	public Map<String, Object> login(Map<String, Object> body);

	public void enterChattingRoom(Map<String, Object> body);

	public void exitChattingRoom(Map<String, Object> body);
}
