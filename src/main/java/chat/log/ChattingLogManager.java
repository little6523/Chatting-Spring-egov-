package chat.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import chat.annotation.chat;

@chat
public class ChattingLogManager {
	
	public List<Map<String, Object>> readChatting(String roomName) {
		return read(roomName);
	}
	
	public void saveChatting(String roomName, Map<String, Object> data) {
		write(roomName, data);
	}
	
    // 파일 읽기
	private List<Map<String, Object>> read(String roomName) {
	    Path path = Paths.get("C:/ChattingLog/" + roomName + ".txt");
	    ObjectMapper mapper = new ObjectMapper();
	    List<Map<String, Object>> chatList = new ArrayList<>();

	    if (!Files.exists(path)) {
	        return chatList; // 파일이 없으면 빈 리스트 반환
	    }

	    try (BufferedReader reader = Files.newBufferedReader(path)) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (!line.trim().isEmpty()) {
	                Map<String, Object> message = mapper.readValue(line, new TypeReference<Map<String, Object>>() {});
	                chatList.add(message);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return chatList;
	}
	
    // 파일 쓰기
	private void write(String roomName, Map<String, Object> data) {
	    Path path = Paths.get("C:/ChattingLog/" + roomName + ".txt");
	    ObjectMapper mapper = new ObjectMapper();

	    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
	        String jsonStr = mapper.writeValueAsString(data);
	        writer.write(jsonStr);
	        writer.newLine(); // 줄바꿈
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private boolean isExistFile(String filePath) {
        File file = new File(filePath);
        
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
	}
}
