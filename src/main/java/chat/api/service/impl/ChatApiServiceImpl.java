package chat.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.api.mapper.ChatApiMapper;
import chat.api.service.ChatApiService;
import chat.socket.ChattingRoomManager;
import chat.socket.Room;
import chat.socket.User;

@Service("ChatApiService")
public class ChatApiServiceImpl extends EgovAbstractServiceImpl implements ChatApiService{

	@Resource(name = "ChatApiMapper")
	private ChatApiMapper chatApiMapper;
	
	@Autowired
	private ChattingRoomManager chattingRoomManager;

	@Override
	public Map<String, Object> test() {
		Map<String, Object> map = chatApiMapper.test();
		return map;
	}
	
	@Override
	public boolean login(Map<String, Object> body) {
		Map<String, Object> map = chatApiMapper.getUser(body);
		if (map == null) {
			return false;
		}
		
		return true;
	}

	@Override
	public int createRoom(Map<String, Object> body) {
		String name = (String) body.get("roomName");
		String userId = (String) body.get("name");
		
		if(chattingRoomManager.getChattingRoom(name) != null) {
			return 0;
		}
		
		Map<String, Object> param = new HashMap<>();
		param.put("name", name);
		param.put("manager", userId);
		chatApiMapper.createRoom(param);
		
        Room room = new Room(name, userId);
        int size = chattingRoomManager.createRoom(name, room);

        return size;
	}
}
