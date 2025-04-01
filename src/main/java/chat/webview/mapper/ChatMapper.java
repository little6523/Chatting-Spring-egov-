package chat.webview.mapper;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("ChatMapper")
public interface ChatMapper {

	List<Map<String, Object>> getChattingRooms();

}
