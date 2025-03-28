package chat.api.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/test")
	public ResponseEntity<Map<String, Object>> hello() {
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> reuslt = chatApiService.test();
		return ResponseEntity.ok().headers(headers).body(reuslt);
	}
}
