package egovframework.com.board.webview.service;

import java.util.List;
import java.util.Map;

public interface BoardService {
	
	public List<Map<String, Object>> selectBoard(Map<String, Object> params);
	
	public Long selectCountPosts();

	public Map<String, Object> selectPost(Long id);
}
