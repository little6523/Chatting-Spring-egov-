package chat.webview.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import chat.webview.mapper.ChatMapper;
import chat.webview.service.ChatService;

@Service("ChatService")
public class ChatServiceImpl extends EgovAbstractServiceImpl implements ChatService{

	@Resource(name = "ChatMapper")
	private ChatMapper chatMapper;
	
	@Override
	public List<Map<String, Object>> getChattingRooms() {
		return chatMapper.getChattingRooms();
		
	}

}
