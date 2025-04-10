package chat.socket;

import javax.websocket.Session;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
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

    public String getName() {
        return name;
    }

    public Session getSession() {
        return session;
    }
}
