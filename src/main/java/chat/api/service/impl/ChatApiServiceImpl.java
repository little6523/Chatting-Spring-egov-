package chat.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.api.mapper.ChatApiMapper;
import chat.api.mapper.UserApiMapper;
import chat.api.service.ChatApiService;
import chat.socket.ChattingRoomManager;
import chat.socket.Room;

@Service("ChatApiService")
public class ChatApiServiceImpl extends EgovAbstractServiceImpl implements ChatApiService{

	@Resource(name = "ChatApiMapper")
	private ChatApiMapper chatApiMapper;
	
	@Resource(name = "UserApiMapper")
	private UserApiMapper userApiMapper;
	
	@Autowired
	private ChattingRoomManager chattingRoomManager;
	
	@Override
	public int createChattingRoom(Map<String, Object> body) {
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
		Map<String, Object> user = userApiMapper.getUserByNickname(body);
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
		Map<String, Object> user = userApiMapper.getUserByNickname(body);
		Map<String, Object> room = chatApiMapper.getRoomByName(body);
		Map<String, Object> param = new HashMap<>();
		param.put("userSeq", user.get("seq"));
		param.put("roomSeq", room.get("seq"));
		chatApiMapper.exitRoom(param);
		
	}

	@Override
	public List<Map<String, Object>> getParticipants(Map<String, Object> body) {
		Map<String, Object> param = new HashMap<>();
		param.put("roomSeq", body.get("roomSeq"));
		List<Map<String, Object>> participants = chatApiMapper.getParticipants(param);
		
		param.clear();
		List<Map<String, Object>> users = new ArrayList<>();
		for (Map<String, Object> m : participants) {
			param.put("userSeq", m.get("user_seq"));
		 	Map<String, Object> user = userApiMapper.getUserBySeq(param);
		 	users.add(user);
		}
		
		return users;
	}
}
