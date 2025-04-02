package chat.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import chat.annotation.chat;

@chat
public class ChattingFileManager {
	
	private Path path = Paths.get("C:/ChattingLog/log.txt");
	
	public void setFilePath(String roomName) {
		this.path = Paths.get("C:/ChattingLog/" + roomName + ".txt");
	}
	
	public void saveChatting(String data) {
		write(data + "\n");
	}
	
    // 파일 읽기
	private String read() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return sb.toString();
	}
	
    // 파일 쓰기
	private void write(String data) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
