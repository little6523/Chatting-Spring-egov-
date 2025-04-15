package chat.webview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/webview")
public class UserController {
	
	@GetMapping("/index")
	public String index() {
		System.out.println("hello");
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
	@GetMapping("/mypage")
	public String myPage(@RequestParam("nickname") String nickname, Model model) {
		model.addAttribute("nicknme", nickname);
		return "mypage";
	}
}
