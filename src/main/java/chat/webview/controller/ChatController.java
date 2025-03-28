package chat.webview.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import chat.webview.service.ChatService;

@Controller
@RequestMapping("/webview")
public class ChatController {

	@Resource(name = "ChatService")
	private ChatService chatService;
	
	@GetMapping("/index")
	public String index() {
		System.out.println("hello");
		return "index";
	}
}
