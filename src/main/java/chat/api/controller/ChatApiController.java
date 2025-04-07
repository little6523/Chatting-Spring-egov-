package chat.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chat.api.service.ChatApiService;

@RestController
@RequestMapping("/api")
public class ChatApiController {

	@Resource(name = "ChatApiService")
	private ChatApiService chatApiService;
	
	private static final String IMAGE_DIR = "C:/ChattingProfileImages/";
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> body) {
		HttpHeaders headers = new HttpHeaders();
		boolean login = chatApiService.login(body);
		Map<String, Object> result = new HashMap<>();
		if (!login) {
			result.put("result", "실패");
			result.put("rspCode", "2");
			return ResponseEntity.ok().headers(headers).body(result);
		}
		
		result.put("reuslt", "성공");
		result.put("rspCode", "1");
		return ResponseEntity.ok().headers(headers).body(result);
	}
	
	@PostMapping("/createRoom")
	public ResponseEntity<Map<String, Object>> createRoom(@RequestBody Map<String, Object> body) {
		System.out.println("방 정보: " + body);
		int roomNumber = chatApiService.createRoom(body);
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		
		if (roomNumber == 0) {
			result.put("rspCode", "1");
			result.put("error", "같은 이름의 방이 이미 존재합니다.");
			return ResponseEntity.ok().headers(headers).body(result);
		}
		
		result.put("rspCode", "1");
		result.put("roomNumber", roomNumber);
		
		return ResponseEntity.ok().headers(headers).body(result);
	}
	
	@PostMapping("/profileImages")
	public ResponseEntity<Map<String, Object>> getImage(@RequestBody Map<String, Object> body) {
        try {
        	String name = (String) body.get("name");
        	
            // 파일 읽기
            Path imagePath = Paths.get(IMAGE_DIR + name + ".jpg");
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // HTTP 응답 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rspCode", "1");
            result.put("image", Base64.getEncoder().encodeToString(imageBytes));
            return ResponseEntity.ok().headers(headers).body(result);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
