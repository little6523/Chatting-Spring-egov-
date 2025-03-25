package chat;

import org.java_websocket.WebSocket;

public class User {
    private String name;
    private String ip;
    private int port;
    private WebSocket webSocket;

    public User(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    // WebSocket 객체를 추가한 생성자
    public User(String name, String ip, int port, WebSocket webSocket) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.webSocket = webSocket;
    }

    public String getName() {
        return name;
    }
    
    public String getIp() {
        return ip;
    }
    
    public int getPort() {
        return port;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
}
