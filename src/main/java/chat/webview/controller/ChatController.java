package chat.webview.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/chat")
	public String chatRoom(Model model) {
	    List<Map<String, Object>> rooms = chatService.getChattingRooms();
	    model.addAttribute("rooms", rooms);
		return "roomlist";
	}
	
	@GetMapping("/chat/rooms")
	public String enterRoom(HttpServletRequest request, @RequestParam("roomName") String roomName, Model model) {
		String clientIP = request.getRemoteAddr();  // 클라이언트 IP
        int clientPort = request.getRemotePort();  // 클라이언트 포트 번호
        System.out.println(clientIP + ":" + clientPort);
        model.addAttribute("roomName", roomName);
		return "client";
	}
	
	@GetMapping("/mypage")
	public String myPage(@RequestParam("nickname") String nickname, Model model) {
		model.addAttribute("nicknme", nickname);
		return "mypage";
	}
}
