package egovframework.com.board.api.mapper;

import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("BoardApiMapper")
public interface BoardApiMapper {
	
	public Long savePost(Map<String, Object> body);
	
	public Long updatePost(Map<String, Object> body);
} 
