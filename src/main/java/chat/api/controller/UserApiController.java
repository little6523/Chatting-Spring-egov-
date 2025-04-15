package chat.api.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chat.api.service.UserApiService;

@RestController
@RequestMapping("/api")
public class UserApiController {
	
	@Resource(name = "UserApiService")
	private UserApiService userApiService;

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> body) {
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> login = userApiService.login(body);
		Map<String, Object> result = new HashMap<>();
		if (login == null) {
			result.put("result", "실패");
			result.put("rspCode", "2");
			return ResponseEntity.ok().headers(headers).body(result);
		}

		result.put("reuslt", "성공");
		result.put("rspCode", "1");
		result.put("seq", login.get("seq"));
		result.put("nickname", (String) login.get("nickname"));
		return ResponseEntity.ok().headers(headers).body(result);
	}
	
	// ID, 닉네임 중복 확인
	@PostMapping("/checkDuplication")
	public ResponseEntity<Map<String, Object>> checkDuplication(@RequestBody Map<String, Object> body) {
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		
		boolean idCheck = false;
		boolean nicknameCheck = false;
		
		if (body.get("id") != null) {
			idCheck = userApiService.checkIdDuplication(body);
			
			if (!idCheck) {
				result.put("result", "동일한 아이디가 존재합니다.");
				result.put("check", "fail");
				result.put("rspCode", "1");
			} else {
				result.put("result", "사용 가능한 아이디입니다.");
				result.put("check", "success");
				result.put("rspCode", "1");
			}
			
			return ResponseEntity.ok().headers(headers).body(result);
		}
		
		if (body.get("nickname") != null) {
			nicknameCheck = userApiService.checkNicknameDuplication(body);
			
			if (!nicknameCheck) {
				result.put("result", "동일한 닉네임이 존재합니다.");
				result.put("check", "fail");
				result.put("rspCode", "1");
			} else {
				result.put("result", "사용 가능한 닉네임입니다.");
				result.put("check", "success");
				result.put("rspCode", "1");
			}
			
			return ResponseEntity.ok().headers(headers).body(result);
		}
		
		result.put("reuslt", "error");
		result.put("rspCode", "2");
		
		return null;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, Object> body) {
		BigInteger userSeq = userApiService.signup(body);
		userApiService.postImage((String) body.get("image"), String.valueOf(userSeq));
		
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rspCode", "1");
		result.put("result", "성공");
		return ResponseEntity.ok().headers(headers).body(result);
	}
	
	// 프로필 이미지를 따로 불러오기 위한 메소드
	@PostMapping("/profileImages")
	public ResponseEntity<Map<String, Object>> getImage(@RequestBody Map<String, Object> body) {
		String imageBytes = userApiService.getImage(body);

		HttpHeaders headers = new HttpHeaders();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rspCode", "1");
		result.put("image", imageBytes);
		return ResponseEntity.ok().headers(headers).body(result);
	}

	@PostMapping("/mypage/update")
	public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, Object> body) {
		String image = (String) body.get("image");
		String userSeq = (String) body.get("userSeq");
		String oldNickname = (String) body.get("oldNickname");
		String newNickname = (String) body.get("newNickname");
		String password = (String) body.get("password");
		
		if (image != null && !"".equals(image)) {
			userApiService.postImage(image, userSeq);
		}
		
		if (password != null && !"".equals(password)) {
			userApiService.changePassword(oldNickname, password);
		}
		
		if (newNickname != null && oldNickname != newNickname) {
			userApiService.changeNickname(oldNickname, newNickname);
		}
		
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		result.put("rspCode", "1");
		return ResponseEntity.ok().headers(headers).body(result);
	}
}
