package coms309.Tickets;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import coms309.Accounts.AccountRepository;
import coms309.Events.EventRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ServerEndpoint("/ticket/price/socket/{eventId}")
@Component
public class LiveTickets {

    private final AccountRepository accountRepository;
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    private static final Logger logger = LoggerFactory.getLogger(LiveTickets.class);
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Autowired
    public LiveTickets(AccountRepository accountRepository, TicketRepository ticketRepository, EventRepository eventRepository) {
        this.accountRepository = accountRepository;
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("eventId") int eventId) {
        sessions.put(String.valueOf(eventId), session);
        logger.info("New WebSocket connection opened for event: {}", eventId);
        sendAverageTicketPrice(eventId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("eventId") String eventId) {
        sessions.remove(eventId);
        logger.info("WebSocket connection closed for event: {}", eventId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("WebSocket error: ", error);
        try {
            session.close();
        } catch (IOException e) {
            logger.error("Error closing session", e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("eventId") int eventId) {
        logger.info("Received message from event {}: {}", eventId, message);
        sendAverageTicketPrice(eventId);
    }

    public void notifyPriceChange(int eventId) {
        sendAverageTicketPrice(eventId);
    }

    private void sendAverageTicketPrice(int eventId) {
        Session session = sessions.get(String.valueOf(eventId));
        if (session != null && session.isOpen()) {
            try {
                double averagePrice = calculateAverageTicketPrice(eventId);
                session.getBasicRemote().sendText(String.format("%.2f", averagePrice));
            } catch (IOException e) {
                logger.error("Error sending average ticket price: ", e);
            }
        }
    }

    private double calculateAverageTicketPrice(int eventId) {
        try {
            List<Ticket> allTicketInEvent = ticketRepository.findAll();
            for (int i = 0 ; i < allTicketInEvent.size() ; i++) {
                Ticket t = allTicketInEvent.get(i);
                if (t.getEvent().getId() != eventId) {
                    allTicketInEvent.remove(i);
                }
            }

            if (allTicketInEvent.isEmpty()) {
                return 0.0;
            }
            return allTicketInEvent.stream()
                    .mapToDouble(Ticket::getPrice)
                    .average()
                    .orElse(0.0);
        } catch (Exception e) {
            logger.error("Error calculating average ticket price: ", e);
            return 0.0;
        }
    }
}