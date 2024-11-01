package coms309.Tickets;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

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

@ServerEndpoint("/ticket/price/{username}")
@Component
public class LiveTickets {
    private static AccountRepository accountRepository;
    private static TicketRepository ticketRepository;
    private static EventRepository eventRepository;




    private static Logger logger = LoggerFactory.getLogger(LiveTickets.class);


    public void setAccountRepository(AccountRepository repo) {
        accountRepository = repo;
    }

    public void setTicketRepository(TicketRepository repo) {
        ticketRepository = repo;
    }
    public void setEventRepository(EventRepository repo) {
        eventRepository = repo;
    }


    private static Map<String, Session> sessions = new Hashtable<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {

    }


    @OnClose
    public void onClose(Session session) {

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

    @OnMessage
    public void onMessage(String message, Session session) {}








}