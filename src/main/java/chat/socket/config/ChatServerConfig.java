package chat.socket.config;

import javax.websocket.server.ServerEndpointConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatServerConfig extends ServerEndpointConfig.Configurator{

	private static ApplicationContext context;
	
	@Autowired
	public void setApplicationContext(ApplicationContext context) {
		ChatServerConfig.context = context;
	}
	
	@Override
	public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
		return context.getBean(endpointClass);
	}
}

