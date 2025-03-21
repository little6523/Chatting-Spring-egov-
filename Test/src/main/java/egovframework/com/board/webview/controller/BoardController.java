package egovframework.com.board.webview.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.board.webview.service.BoardService;

@Controller
@RequestMapping("/webview")
public class BoardController {
	
	@Resource(name = "BoardService")
	private BoardService boardService;
	
	// 게시글 리스트 페이지
	@GetMapping("/board.do")
	public String getBoard(
			@RequestParam(defaultValue = "1") int page,
			Model model) {
		Map<String, Object> params = new HashMap<>();
		Long totalPosts = boardService.selectCountPosts();
		params.put("page", page);
		params.put("totalPosts", totalPosts);
		model.addAttribute("currentPage", page);
		model.addAttribute("posts", boardService.selectBoard(params));
		model.addAttribute("totalPosts", totalPosts);
		return "egovframework/com/board/board";
	}
	
	// 게시글 페이지 이동
//	@PostMapping("/board.json")
//	@ResponseBody
//	public ResponseEntity<Map<String, Object>> getBoardPaging(@RequestBody Map<String, Object> params, Model model) {
//		if (params != null) {
//			HttpHeaders headers = new HttpHeaders();
//			Map<String, Object> result = new HashMap<>();
//			result.put("posts", boardService.selectBoard(params));
//			result.put("rspCode", "1");
//			return ResponseEntity.ok().headers(headers).body(result);
//		}
//		return null;
//	}
	
	// 게시글 작성 페이지
	@GetMapping("/postWrite.do")
	public String getPostWrite(Model model) {
		return "egovframework/com/board/postWrite";
	}
	
	// 게시글 상세 페이지
	@GetMapping("/board.do/{id}")
	public String getPostDetail(@PathVariable("id") Long id,
			@RequestParam(required = false) Integer page,
			Model model) {
		Map<String, Object> res = boardService.selectPost(id);
		if (res == null || res.isEmpty()) {
			return "해당 id를 가진 글이 존재하지 않습니다.";
		}
		
		if (page != null) {
			model.addAttribute("page", page.intValue());
		}
		model.addAttribute("data", res);
		return "egovframework/com/board/postDetail";
	}
	
	// 게시글 수정 페이지
	@GetMapping("/postWrite.do/{id}")
	public String getPostUpdate(@PathVariable("id") Long id,
			@RequestParam(required = false) Integer page,
			Model model) {
		if (page != null) {
			model.addAttribute("page", page.intValue());
		}
		model.addAttribute("post", boardService.selectPost(id));
		return "egovframework/com/board/postWrite";
	}
}
