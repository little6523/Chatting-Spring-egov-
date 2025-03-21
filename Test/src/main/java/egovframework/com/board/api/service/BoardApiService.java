package egovframework.com.board.api.service;

import java.util.Map;

public interface BoardApiService {

	public Long savePost(Map<String, Object> body);
	
	public Long updatePost(Map<String, Object> body);

	public void saveDummyPosts(int num);
}
