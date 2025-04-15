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
import chat.api.mapper.UserApiMapper;
import chat.api.service.ChatApiService;
import chat.api.service.UserApiService;
import chat.socket.ChattingRoomManager;
import chat.socket.Room;

@Service("UserApiService")
public class UserApiServiceImpl extends EgovAbstractServiceImpl implements UserApiService{
	
	private static final String IMAGE_DIR = "C:/ChattingProfileImages/";

	@Resource(name = "UserApiMapper")
	private UserApiMapper userApiMapper;
	
	@Override
	public Map<String, Object> login(Map<String, Object> body) {
		Map<String, Object> map = userApiMapper.login(body);
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
        	String image = userApiMapper.getImagePath(param);
        	
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

            String saveFileName = userSeq;

            Path saveFilePath = path.resolve(saveFileName);

            Files.write(saveFilePath, decodedBytes);
            
            Map<String, Object> param = new HashMap<>();
    		param.put("imagePath", saveFilePath.toString());
    		param.put("userSeq", userSeq);
    		userApiMapper.changeImagePath(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void changeNickname(String oldNickname, String newNickname) {
		Map<String, Object> param = new HashMap<>();
		param.put("oldNickname", oldNickname);
		param.put("newNickname", newNickname);
		userApiMapper.changeNickname(param);
	}

	@Override
	public void changePassword(String oldNickname, String password) {
		Map<String, Object> param = new HashMap<>();
		param.put("oldNickname", oldNickname);
		param.put("password", password);
		userApiMapper.changePassword(param);
	}
}
