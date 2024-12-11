package coms309.PostTickets;

import java.io.IOException;
import java.util.*;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
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

@Controller
@ServerEndpoint(value = "/tickets/{event_id}/{user_id}")
public class PostTicketSocket {

    private static AccountRepository accountRepository;
    private static TicketRepository ticketRepository;
    private static EventRepository eventRepository;

    @Autowired
    public void setAccountRepository(AccountRepository repo) {
        accountRepository = repo;
    }
    @Autowired
    public void setTicketRepository(TicketRepository repo) {
        ticketRepository = repo;
    }
    @Autowired
    public void setEventRepository(EventRepository repo) {
        eventRepository = repo;
    }

    // Track sessions for each event
    private static final Map<Integer, Set<Session>> eventSessions = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(PostTicketSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("event_id") int eventId, @PathParam("user_id") int userId) throws IOException {
        logger.info("User {} joined event {}.", userId, eventId);

        eventSessions.computeIfAbsent(eventId, k -> new HashSet<>()).add(session);

        Event event = eventRepository.findById(eventId);

        session.getBasicRemote().sendText("Welcome to event: " + event.getName() + "!");
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("event_id") int eventId, @PathParam("user_id") int userId, String message) throws IOException {
        logger.info("User {} posted to event {}: {}", userId, eventId, message);

        Account account = accountRepository.findById(userId);

        Ticket toPost;
        String response = "Internal Failure";
        if(message.split(" ").length > 1 && message.split(" ")[1].equals("PURCHASE")){
            response = account.getUsername() + " bought ticket " + message.split(" ")[0] + "!";
        }
        else{
            int ticketID = Integer.parseInt(message);
            toPost = ticketRepository.findById(ticketID);
            if(!toPost.isIs_active()) {
                toPost.setAccount(null);
                toPost.setEvent(eventRepository.findById(eventId));
                toPost.setIs_active(true);
                ticketRepository.save(toPost);
                response = account.getUsername() + " posted ticket " + ticketID + "!";

            }

        }

        Set<Session> sessions = eventSessions.get(eventId);
        if (sessions != null) {
            for (Session s : sessions) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendText(response);
                }
            }
        }

    }

    @OnClose
    public void onClose(Session session, @PathParam("event_id") int eventId) throws IOException {
        logger.info("Session closed for event {}.", eventId);

        Set<Session> sessions = eventSessions.get(eventId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                eventSessions.remove(eventId);
            }
        }

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("WebSocket error: ", throwable);
    }
}
