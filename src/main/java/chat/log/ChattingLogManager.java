package chat.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import chat.annotation.chat;

@chat
public class ChattingLogManager {
	
	public String readChatting(String roomName) {
		return read(roomName);
	}
	
	public void saveChatting(String roomName, String data) {
		write(roomName, data + "\n");
	}
	
    // 파일 읽기
	private String read(String roomName) {
        StringBuilder sb = new StringBuilder();
        String pathString = "C:/ChattingLog/" + roomName + ".txt";
        
        if(isExistFile(pathString)) {
        	Path path = Paths.get(pathString);
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return null;
	}
	
    // 파일 쓰기
	private void write(String roomName, String data) {
		Path path = Paths.get("C:/ChattingLog/" + roomName + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(data);
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
