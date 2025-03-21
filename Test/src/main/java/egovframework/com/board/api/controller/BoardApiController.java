package egovframework.com.board.api.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import egovframework.com.board.api.service.BoardApiService;

@RestController
@RequestMapping("/api")
public class BoardApiController {

	@Resource(name = "BoardApiService")
	private BoardApiService boardApiService;

	// 게시글 작성
	@PostMapping("/board")
	public ResponseEntity<Map<String, Object>> sendPost(@RequestBody Map<String, Object> body) {
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		Long res = boardApiService.savePost(body);
		result.put("rspCode", "1");
		return ResponseEntity.created(URI.create("/" + res)).headers(headers).body(result);
	}

	// 이미지 업로드
	@PostMapping("/board/image")
	public void imageUpload(HttpServletRequest req, HttpServletResponse res, @RequestParam MultipartFile upload) {

	/*
	 * OutputStream out = null; PrintWriter printWriter = null;
	 * 
	 * try { // 랜덤 문자 생성 UUID uid = UUID.randomUUID(); // 이름, 확장자, 바이트 가져오기 String
	 * fileName = upload.getOriginalFilename(); String extension =
	 * FilenameUtils.getExtension(fileName); byte[] bytes = upload.getBytes();
	 * 
	 * // 실제 이미지 저장 경로 String path = "C:/upload/" + File.separator; String
	 * ckUploadPath = path + uid + "." + extension;
	 * 
	 * System.out.println("ckUploadPath :" + ckUploadPath);
	 * 
	 * File folder = new File(path); // 해당 디렉토리가 존재하는지 확인 if (!folder.exists()) {
	 * try { folder.mkdirs(); // 폴더 생성 } catch (Exception e) { e.getStackTrace(); }
	 * }
	 * 
	 * // 이미지 저장 out = new FileOutputStream(ckUploadPath); out.write(bytes);
	 * out.flush(); // 초기화
	 * 
	 * // ckEditor 로 전송 printWriter = res.getWriter(); fileName = uid + "." +
	 * extension; String fileUrl = "/upload/" + fileName;
	 * 
	 * System.out.println("fileUrl :" + fileUrl);
	 * 
	 * // json 으로 변환 JsonObject json = new JsonObject();
	 * json.addProperty("uploaded", 1); json.addProperty("fileName", fileName);
	 * json.addProperty("url", fileUrl); printWriter.println(json);
	 * printWriter.flush(); // 초기화
	 * 
	 * System.out.println("json :" + json);
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } finally { try { if (out !=
	 * null) { out.close(); } if (printWriter != null) { printWriter.close(); } }
	 * catch (IOException e) { e.printStackTrace(); } } }
	 */
	}

	// 게시글 수정
	@PatchMapping("/board/{id}")
	public ResponseEntity<Map<String, Object>> updatePost(@PathVariable("id") Long id,
			@RequestBody Map<String, Object> body) {
		HttpHeaders headers = new HttpHeaders();
		Map<String, Object> result = new HashMap<>();
		body.put("id", id);
		Long res = boardApiService.updatePost(body);
		result.put("rspCode", "1");
		result.put("id", res);
		return ResponseEntity.ok().headers(headers).body(result);
	}

	// 더미 게시글 생성 (num: 생성하고자 하는 게시글 개수)
	@PostMapping("/dummy")
	public void saveDummyPosts(@RequestBody Map<String, Object> body) {
		boardApiService.saveDummyPosts((int) body.get("num"));
	}
}
