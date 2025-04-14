package chat.api.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("ChatApiMapper")
public interface ChatApiMapper {
	
	public Map<String, Object> login(Map<String, Object> param);
	
	public Map<String, Object> getUserBySeq(Map<String, Object> param);
	
	public Map<String, Object> getUserByNickname(Map<String, Object> param);
	
	public String getImagePath(Map<String, Object> param);
	
	public void changeImagePath(Map<String, Object> param);
	
	public void changeNickname(Map<String, Object> param);
	
	public void changePassword(Map<String, Object> param);
	
	public Map<String, Object> getRoomByName(Map<String, Object> param);
	
	public List<Map<String, Object>> getParticipants(Map<String, Object> param);
	
	public Map<String, Object> getParticipationInfo(Map<String, Object> param);
	
	public void createRoom(Map<String, Object> param);

	public void enterRoom(Map<String, Object> param);

	public void exitRoom(Map<String, Object> param);
}
