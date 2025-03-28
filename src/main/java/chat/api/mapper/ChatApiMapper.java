package chat.api.mapper;

import java.util.Map;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("ChatApiMapper")
public interface ChatApiMapper {

	public Map<String, Object> test();

}
