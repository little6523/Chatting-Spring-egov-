package chat.api.service;

import java.math.BigInteger;
import java.util.Map;

public interface UserApiService {

	public Map<String, Object> login(Map<String, Object> body);
	
	public boolean checkIdDuplication(Map<String, Object> body);
	
	public BigInteger signup(Map<String, Object> body);
	
	public boolean checkNicknameDuplication(Map<String, Object> body);
	
	public String getImage(Map<String, Object> body);
	
	public void postImage(String image, String oldNickname);
	
	public void changeNickname(String oldNickname, String newNickname);

	public void changePassword(String oldNickname, String password);

}
