package chat.webview.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
	
	@GetMapping("/chat")
	public String chat() {
		return "login";
	}
	
	@GetMapping("/chat/rooms")
	public String chatRoom() {
		return "roomlist";
	}
	
	@GetMapping("/chat/rooms/1")
	public String room(HttpServletRequest request) {
		String clientIP = request.getRemoteAddr();  // 클라이언트 IP
        int clientPort = request.getRemotePort();  // 클라이언트 포트 번호
        System.out.println(clientIP + ":" + clientPort);
		return "client";
	}
}
