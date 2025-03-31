package chat.api.service;

import java.util.Map;

import chat.socket.User;

public interface ChatApiService {

	public Map<String, Object> test();

	public int createRoom(Map<String, Object> body);

	public boolean login(Map<String, Object> body);
}
