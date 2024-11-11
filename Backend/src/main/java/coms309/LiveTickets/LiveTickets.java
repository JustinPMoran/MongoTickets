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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

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

    @Autowired
    public static void setRepositories(TicketRepository ticketRepository, EventRepository eventRepository, LiveRepository liveRepository) {
        LiveTickets.ticketRepository = ticketRepository;
        LiveTickets.eventRepository = eventRepository;
        LiveTickets.liveRepository = liveRepository;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("eventId") int eventId) throws IOException {
        eventSessions.computeIfAbsent(eventId, k -> new HashSet<>()).add(session);
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

    @OnMessage
    public void onMessage(String message, @PathParam("eventId") int eventId, Session session) {
        sendAverageTicketPrice(eventId, session);
    }

    private static void sendAverageTicketPrice(int eventId, Session specificSession) {
        Set<Session> sessions = eventSessions.get(eventId);
        if (sessions != null) {
            try {
                double newAveragePrice = calculateAverageTicketPrice(eventId);
                if (specificSession != null && specificSession.isOpen()) {
                    specificSession.getAsyncRemote().sendText(String.valueOf(newAveragePrice));
                } else {
                    for (Session session : sessions) {
                        if (session.isOpen()) {
                            session.getAsyncRemote().sendText(String.valueOf(newAveragePrice));
                        }
                    }
                }

            } catch (Exception e) {
                logger.error("Error sending average ticket price: ", e);
            }
        }
    }

    private static double calculateAverageTicketPrice(int eventId) {
        try {

            Event event = eventRepository.findById(eventId);
            if (event == null) {
                return -1.0;
            }
            List<Ticket> tickets = ticketRepository.findAll();
            List<Ticket> ticks = new ArrayList<>();

            System.out.println("Ticketsize:" + tickets.size());
            for (Ticket ticket : tickets) {
                ticks.add(ticket);
                if (ticket.getEvent() != null && ticket.getEvent().getId() != eventId) {
                    ticks.remove(ticket);
                }


            }

            double totalPrice = 0.0;
            if (ticks.isEmpty()) {
                return 0.0;
            }
            for (Ticket ticket : ticks) {
                totalPrice += ticket.getPrice();
            }
            return totalPrice / ticks.size();

        } catch (Exception e) {
            logger.error("Error calculating average ticket price: ", e);
            return -1.0;
        }
    }


    public static void broadcast(String message, int eventId) {
        Set<Session> sessions = eventSessions.get(eventId);
        System.out.println(eventId);
        if (sessions == null) {
            return;
        }
        for (Session session : sessions ) {

            try {

                if (session != null && session.isOpen()) {
                    sendAverageTicketPrice(eventId, session);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
