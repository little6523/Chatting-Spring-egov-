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
	public Map<String, Object> login(Map<String, Object> body) {
		Map<String, Object> map = chatApiMapper.login(body);
		if (map == null) {
			return null;
		}
		
		return map;
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

	@Override
	public void enterChattingRoom(Map<String, Object> body) {
		Map<String, Object> user = chatApiMapper.getUserByNickname(body);
		Map<String, Object> room = chatApiMapper.getRoomByName(body);
		Map<String, Object> param = new HashMap<>();
		param.put("userSeq", user.get("seq"));
		param.put("roomSeq", room.get("seq"));
		
		// 방에 참여한 상태가 아니면 방에 참여 처리
		if (chatApiMapper.getParticipationInfo(param) == null) {
			chatApiMapper.enterRoom(param);
		}
	}

	@Override
	public void exitChattingRoom(Map<String, Object> body) {
		Map<String, Object> user = chatApiMapper.getUserByNickname(body);
		Map<String, Object> room = chatApiMapper.getRoomByName(body);
		Map<String, Object> param = new HashMap<>();
		param.put("userSeq", user.get("seq"));
		param.put("roomSeq", room.get("seq"));
		chatApiMapper.exitRoom(param);
		
	}
}
