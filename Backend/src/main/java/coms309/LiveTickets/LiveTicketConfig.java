package coms309.LiveTickets;
import coms309.Events.EventRepository;
import coms309.LiveTickets.LiveRepository;
import coms309.Tickets.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class LiveTicketConfig {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LiveRepository liveRepository;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @PostConstruct
    public void configureWebSocket() {
        // Set the static references for the LiveTickets class
        LiveTickets.setRepositories(ticketRepository, eventRepository, liveRepository);
    }
}
