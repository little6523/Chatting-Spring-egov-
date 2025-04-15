package chat.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
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
	
	@PostMapping("/participants")
	public ResponseEntity<Map<String, Object>> getParticipants(@RequestBody Map<String, Object> body) {
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		result.put("rspCode", "1");
		result.put("participants", chatApiService.getParticipants(body));
		return ResponseEntity.ok().headers(headers).body(result);
	}

	@PostMapping("/createRoom")
	public ResponseEntity<Map<String, Object>> createRoom(@RequestBody Map<String, Object> body) {
		System.out.println("방 정보: " + body);
		int roomSeq = chatApiService.createChattingRoom(body);
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();

		if (roomSeq == 0) {
			result.put("rspCode", "1");
			result.put("error", "같은 이름의 방이 이미 존재합니다.");
			return ResponseEntity.ok().headers(headers).body(result);
		}

		result.put("rspCode", "1");
		result.put("roomSeq", roomSeq);

		return ResponseEntity.ok().headers(headers).body(result);
	}

	@PostMapping("/enterRoom")
	public ResponseEntity<Map<String, Object>> enterRoom(@RequestBody Map<String, Object> body) {
		chatApiService.enterChattingRoom(body);
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();

		result.put("rspCode", "1");
		return ResponseEntity.ok().headers(headers).body(result);
	}

	@PostMapping("/exitRoom")
	public ResponseEntity<Map<String, Object>> exitRoom(@RequestBody Map<String, Object> body) {
		chatApiService.exitChattingRoom(body);
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();

		result.put("rspCode", "1");
		return ResponseEntity.ok().headers(headers).body(result);
	}
}
