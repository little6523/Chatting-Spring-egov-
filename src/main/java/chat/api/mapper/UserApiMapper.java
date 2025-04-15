package chat.api.mapper;

import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("UserApiMapper")
public interface UserApiMapper {
	
	public Map<String, Object> login(Map<String, Object> param);
	
	public int signup(Map<String, Object> body);
	
	public Map<String, Object> getUserBySeq(Map<String, Object> param);
	
	public Map<String, Object> getUserById(Map<String, Object> body);
	
	public Map<String, Object> getUserByNickname(Map<String, Object> param);
	
	public String getImagePath(Map<String, Object> param);
	
	public void changeImagePath(Map<String, Object> param);
	
	public void changeNickname(Map<String, Object> param);
	
	public void changePassword(Map<String, Object> param);
}
