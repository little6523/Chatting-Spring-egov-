package chat.api.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import chat.api.mapper.ChatApiMapper;
import chat.api.service.ChatApiService;

@Service("ChatApiService")
public class ChatApiServiceImpl extends EgovAbstractServiceImpl implements ChatApiService{

	@Resource(name = "ChatApiMapper")
	private ChatApiMapper chatApiMapper;

	@Override
	public Map<String, Object> test() {
		Map<String, Object> map = chatApiMapper.test();
		return map;
	}
	
	
}
