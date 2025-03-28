package egovframework.com.board.webview.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("BoardMapper")
public interface BoardMapper {

	public List<Map<String, Object>> selectBoard(Map<String, Object> paramMap);
	
	public Long selectCountPosts();

	public Map<String, Object> selectPost(Map<String, Object> paramMap);

}
