package coms309.LiveTickets;

import coms309.Events.Event;
import coms309.Events.EventRepository;
import coms309.Tickets.Ticket;
import coms309.Tickets.TicketRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller // Needed to register this with Spring Boot
@ServerEndpoint("/ticket/price/socket/{eventId}") // WebSocket endpoint URL
public class LiveTickets {

    private static TicketRepository ticketRepository;
    private static EventRepository eventRepository;
    private static LiveRepository liveRepository;

    // Logger for debug info
    private static final Logger logger = LoggerFactory.getLogger(LiveTickets.class);
    // Sessions are stored per eventId
    private static final Map<Integer, Set<Session>> eventSessions = new HashMap<>();
    // Track the last known average price for each event to detect changes
    private static final Map<Integer, Double> lastKnownAveragePrice = new HashMap<>();

    // Static setter methods to inject dependencies into the WebSocket
    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        LiveTickets.ticketRepository = ticketRepository;
    }

    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        LiveTickets.eventRepository = eventRepository;
    }

    @Autowired
    public void setLiveRepository(LiveRepository liveRepository) {
        LiveTickets.liveRepository = liveRepository;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("eventId") int eventId) throws IOException {
        eventSessions.computeIfAbsent(eventId, k -> new HashSet<>()).add(session);
        logger.info("WebSocket connection opened for event: {}", eventId);
        sendAverageTicketPrice(eventId, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("eventId") int eventId) {
        Set<Session> sessions = eventSessions.get(eventId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                eventSessions.remove(eventId); // Clean up if no more sessions exist for this event
            }
        }
        logger.info("WebSocket connection closed for event: {}", eventId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("WebSocket error: ", throwable);
        try {
            session.close();
        } catch (IOException e) {
            logger.error("Error closing session: ", e);
        }
    }

    // Invoked when a message is received
    @OnMessage
    public void onMessage(String message, @PathParam("eventId") int eventId) {
        logger.info("Received message for event {}: {}", eventId, message);
        sendAverageTicketPrice(eventId, null);
    }

    // Send average ticket price to either a specific session or all sessions for the given eventId
    private void sendAverageTicketPrice(int eventId, Session specificSession) {
        Set<Session> sessions = eventSessions.get(eventId);
        if (sessions != null) {
            try {
                double newAveragePrice = calculateAverageTicketPrice(eventId);
                Double lastPrice = lastKnownAveragePrice.get(eventId);

                // If there's a change in the average price, save a record
                if (lastPrice == null || lastPrice != newAveragePrice) {
                    if (lastPrice != null) {
                        Price priceRecord = new Price(eventId, lastPrice, newAveragePrice);
                        liveRepository.save(priceRecord);
                    }
                    lastKnownAveragePrice.put(eventId, newAveragePrice);
                }

                String priceMessage = String.format("Average Price: %.2f", newAveragePrice);

                if (specificSession != null && specificSession.isOpen()) {
                    specificSession.getAsyncRemote().sendText(priceMessage);
                } else {
                    for (Session session : sessions) {
                        if (session.isOpen()) {
                            session.getAsyncRemote().sendText(priceMessage);
                        }
                    }
                }

            } catch (Exception e) {
                logger.error("Error sending average ticket price: ", e);
            }
        }
    }

    // Calculate the average price of tickets for an event
    private double calculateAverageTicketPrice(int eventId) {

//        try {
//            List<Ticket> allTickets = ticketRepository.findAll();
//            allTickets.removeIf(ticket -> ticket.getEvent().getId() != eventId);
//
//            if (allTickets.isEmpty()) {
//                return 0.0;
//            }
//            return allTickets.stream()
//                    .mapToDouble(Ticket::getPrice)
//                    .average()
//                    .orElse(0.0);
//        } catch (Exception e) {
//            logger.error("Error calculating average ticket price: ", e);
//            return 0.0;
//        }
        double totalPrice = 0;

        Event event = eventRepository.findById(eventId);
        List<Ticket> tickets = event.getTickets();
        if (tickets.isEmpty()) {
            return 0.0;
        }

        for (Ticket ticket : tickets) {
            double price = ticket.getPrice();
            totalPrice += price;
        }
        return (double) totalPrice / tickets.size();

    }
}
