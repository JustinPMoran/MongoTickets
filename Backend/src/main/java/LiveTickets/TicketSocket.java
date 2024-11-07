package LiveTickets;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class TicketSocket implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler() {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                        
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

                    }

                    @Override
                    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

                    }

                    @Override
                    public boolean supportsPartialMessages() {
                        return false;
                    }
                }, "/ticket/price/socket/{eventId}")
                .setAllowedOrigins("*"); // Allow all origins (for testing)
    }
}