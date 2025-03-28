package chat.socket;

import javax.annotation.PostConstruct;

import annotation.chat;

@chat
public class Chat {
	
    // @Value를 사용하여 application.properties에 정의된 값을 주입받음
//    public void ChatServer(@Value("${server.address}") String SERVER_ADDRESS, 
//                      @Value("${server.port}") int SERVER_PORT) {
//        this.SERVER_ADDRESS = SERVER_ADDRESS;
//        this.SERVER_PORT = SERVER_PORT;
//    }

	@PostConstruct
	public void runChattingServer() {
		new ChatServer();
		System.out.println("채팅 서버가 실행 중입니다...");
	}
}
