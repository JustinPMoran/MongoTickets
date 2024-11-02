package coms309.Tickets;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;

import coms309.Accounts.AccountRepository;
import coms309.Events.EventRepository;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint("/ticket/price/socket/{eventId}")
@Component
public class LiveTickets {
    private static AccountRepository accountRepository;
    private static TicketRepository ticketRepository;
    private static EventRepository eventRepository;

    private static Logger logger = LoggerFactory.getLogger(LiveTickets.class);

    private static Map<String, Session> sessions = new Hashtable<>();

    public void setAccountRepository(AccountRepository repo) {
        accountRepository = repo;
    }

    public void setTicketRepository(TicketRepository repo) {
        ticketRepository = repo;
    }

    public void setEventRepository(EventRepository repo) {
        eventRepository = repo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("eventId") int eventId) {
        sessions.put(eventId, session);
        logger.info("New WebSocket connection opened for event: " + eventId);
        sendAverageTicketPrice(eventId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("eventId") String eventId) {
        sessions.remove(eventId);
        logger.info("WebSocket connection closed for event: " + eventId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("WebSocket error: " + error.getMessage());
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("eventId") int eventId) {
        logger.info("Received message from event " + eventId + ": " + message);
        // You can add logic here to handle incoming messages if needed
    }

    public static void notifyPriceChange(int eventId) {
        sendAverageTicketPrice(eventId);
    }

    private static void sendAverageTicketPrice(int eventId) {
        Session session = sessions.get(eventId);
        if (session != null && session.isOpen()) {
            try {
                double averagePrice = calculateAverageTicketPrice(eventId);
                session.getBasicRemote().sendText(String.format("%.2f", averagePrice));
            } catch (IOException e) {
                logger.error("Error sending average ticket price: " + e.getMessage());
            }
        }
    }

    private static double calculateAverageTicketPrice(int eventId) {
        List<Ticket> tickets = ticketRepository;

        if (tickets.isEmpty()) {
            return 0.0;
        }
        double totalPrice = tickets.stream().mapToDouble(Ticket::getPrice).sum();
        return totalPrice / tickets.size();
    }
}