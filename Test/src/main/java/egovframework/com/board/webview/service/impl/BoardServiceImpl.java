package egovframework.com.board.webview.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.com.board.webview.mapper.BoardMapper;
import egovframework.com.board.webview.service.BoardService;

@Service("BoardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {
	
	@Resource(name = "BoardMapper")
	private BoardMapper boardMapper;
	
	// 게시글 리스트 조회
	public List<Map<String, Object>> selectBoard(Map<String, Object> params) {
		
		// 원하는 위치에서 게시글 가져오기
		int page = (int) params.get("page");
		Long totalPosts = (Long) params.get("totalPosts");
		Long offset = totalPosts - (page * 10);
		if (offset >= 0) {
			params.put("offset", offset);
			params.put("limit", Long.valueOf(10));
		} else {
			params.put("offset", Long.valueOf(0));
			params.put("limit", 10 + offset);
		}
		List<Map<String, Object>> posts = boardMapper.selectBoard(params);

		for (Map<String, Object> post : posts) {
			post.put("created_at", post.get("created_at").toString());
			post.put("updated_at", post.get("updated_at").toString());
		}
		
		// 게시글을 최신순으로 가져오기 위해 역순으로 재배열
		Collections.reverse(posts);
		return posts;
	}
	
	// 전체 게시글 개수 조회
	@Override
	public Long selectCountPosts() {
		return boardMapper.selectCountPosts();
	}

	// 특정 id 게시글 조회
	@Override
	public Map<String, Object> selectPost(Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return boardMapper.selectPost(map);
	}
	
}
