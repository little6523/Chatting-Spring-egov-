package chat.socket;

import javax.websocket.Session;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
//	private String id;
//	private String password;
    private String name;
    
    @JsonIgnore
    private Session session;
    
    public User(String name) {
    	this.name = name;
    }
    
    public User(String name, Session session) {
    	this.name = name;
    	this.session = session;
    }

//    public User(String name, String ip, int port) {
//        this.name = name;
//        this.ip = ip;
//        this.port = port;
//    }

    // WebSocket 객체를 추가한 생성자
//    public User(String name, String ip, int port, Session session) {
//        this.name = name;
//        this.ip = ip;
//        this.port = port;
//        this.session = session;
//    }

    public String getName() {
        return name;
    }

    public Session getSession() {
        return session;
    }
}
