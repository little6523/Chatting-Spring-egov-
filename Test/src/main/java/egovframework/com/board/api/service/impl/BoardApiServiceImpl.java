package egovframework.com.board.api.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.com.board.api.mapper.BoardApiMapper;
import egovframework.com.board.api.service.BoardApiService;

@Service("BoardApiService")
public class BoardApiServiceImpl extends EgovAbstractServiceImpl implements BoardApiService {
	
	@Resource(name = "BoardApiMapper")
	private BoardApiMapper boardApiMapper;

	// 게시글 저장
	public Long savePost(Map<String, Object> body) {
		body.put("created_at", Date.valueOf(LocalDate.now()));
		body.put("updated_at", Date.valueOf(LocalDate.now()));
		boardApiMapper.savePost(body);

		return ((Number) body.get("id")).longValue();
	}
	
	// 게시글 수정
	public Long updatePost(Map<String, Object> body) {
		body.put("updated_at", Date.valueOf(LocalDate.now()));
		boardApiMapper.updatePost(body);

		return ((Number) body.get("id")).longValue();
	}

	// 더미 게시글 저장
	@Override
	public void saveDummyPosts(int num) {
		for (int i = 0; i < num; i++) {
			Map<String, Object> post = new HashMap<>();
			post.put("title", "title: dummy text");
			post.put("writer", "이현준");
			post.put("content", "content: dummy text");
			post.put("created_at", Date.valueOf(LocalDate.now()));
			post.put("updated_at", Date.valueOf(LocalDate.now()));
			boardApiMapper.savePost(post);
		}
	}
}
