package chat.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import chat.api.service.ChatApiService;

@RestController
@RequestMapping("/api")
public class ChatApiController {

	@Resource(name = "ChatApiService")
	private ChatApiService chatApiService;
	
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
}
