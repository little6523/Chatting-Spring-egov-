package chat.api.service;

import java.util.List;
import java.util.Map;

import chat.socket.User;

public interface ChatApiService {

	public int createChattingRoom(Map<String, Object> body);

	public void enterChattingRoom(Map<String, Object> body);

	public void exitChattingRoom(Map<String, Object> body);

	public List<Map<String, Object>> getParticipants(Map<String, Object> body);
}
