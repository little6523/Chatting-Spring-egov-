package chat.api.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.api.mapper.ChatApiMapper;
import chat.api.service.ChatApiService;
import chat.socket.ChattingRoomManager;
import chat.socket.Room;

@Service("ChatApiService")
public class ChatApiServiceImpl extends EgovAbstractServiceImpl implements ChatApiService{

	@Resource(name = "ChatApiMapper")
	private ChatApiMapper chatApiMapper;
	
	@Autowired
	private ChattingRoomManager chattingRoomManager;
	
	private static final String IMAGE_DIR = "C:/ChattingProfileImages/";
	
	@Override
	public Map<String, Object> login(Map<String, Object> body) {
		Map<String, Object> map = chatApiMapper.login(body);
		if (map == null) {
			return null;
		}
		
		return map;
	}
	
	@Override
	public String getImage(Map<String, Object> body) {
        try {
        	Map<String, Object> param = new HashMap<String, Object>();
        	param.put("userSeq", Integer.parseInt((String) body.get("userSeq")));
        	String image = chatApiMapper.getImagePath(param);
        	
            Path imagePath = Paths.get(image + ".jpg");
            byte[] imageBytes = Files.readAllBytes(imagePath);
            
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            return "이미지 읽기 오류";
        }
	}

	@Override
	public void postImage(String image, String userSeq) {
        try {
            // 디렉토리 없으면 생성
            Path path = Paths.get(IMAGE_DIR);
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
            
            byte[] decodedBytes = Base64.getDecoder().decode(image);

            String saveFileName = userSeq + ".jpg";

            Path saveFilePath = path.resolve(saveFileName);

            Files.write(saveFilePath, decodedBytes);
            
            Map<String, Object> param = new HashMap<>();
    		param.put("imagePath", saveFilePath.toString());
    		param.put("userSeq", userSeq);
    		chatApiMapper.changeImagePath(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void changeNickname(String oldNickname, String newNickname) {
		Map<String, Object> param = new HashMap<>();
		param.put("oldNickname", oldNickname);
		param.put("newNickname", newNickname);
		chatApiMapper.changeNickname(param);
	}

	@Override
	public void changePassword(String oldNickname, String password) {
		Map<String, Object> param = new HashMap<>();
		param.put("oldNickname", oldNickname);
		param.put("password", password);
		chatApiMapper.changePassword(param);
	}
	
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

	@Override
	public List<Map<String, Object>> getParticipants(Map<String, Object> body) {
		Map<String, Object> param = new HashMap<>();
		param.put("roomSeq", body.get("roomSeq"));
		List<Map<String, Object>> participants = chatApiMapper.getParticipants(param);
		
		param.clear();
		List<Map<String, Object>> users = new ArrayList<>();
		for (Map<String, Object> m : participants) {
			param.put("userSeq", m.get("user_seq"));
		 	Map<String, Object> user = chatApiMapper.getUserBySeq(param);
		 	users.add(user);
		}
		
		return users;
	}
}
