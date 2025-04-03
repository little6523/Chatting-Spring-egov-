package chat.test;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chat.log.ChattingFileManager;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private ChattingFileManager chattingFileManager;
	
	@PostMapping("/chatlog")
	public void createChatLog(@RequestBody Map<String, Object> body) {
//		chattingFileManager.saveChatting(body);
	}
}
