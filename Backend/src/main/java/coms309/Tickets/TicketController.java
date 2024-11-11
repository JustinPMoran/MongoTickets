package coms309.Tickets;

import java.util.List;

import coms309.Accounts.Account;
import coms309.Accounts.AccountController;
import coms309.Events.Event;
import coms309.Events.EventRepository;
import coms309.LiveTickets.LiveTickets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import coms309.Accounts.AccountRepository;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

@RestController
public class TicketController {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EventRepository eventRepository;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    @Autowired
    private LiveTickets liveTickets;

    @GetMapping(path = "/tickets")
    List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    @PostMapping(path = "/tickets/{eventId}")
    String createTicket(@RequestBody Ticket ticket, @PathVariable int eventId){
        Event event = eventRepository.findById(eventId);
        if (ticket == null)
            return failure;
        ticket.setEvent(eventRepository.findById(eventId));
        event.addTicket(ticket);

//



        eventRepository.save(event);
        LiveTickets.broadcast("", eventId);

        return success;
    }

    @DeleteMapping(path = "/tickets")
    String deleteUsers(){
        ticketRepository.deleteAll();
        return success;
    }

    @GetMapping(path = "/tickets/{id}")
    Ticket getTicketById(@PathVariable int id){
        return ticketRepository.findById(id);
    }


    @PutMapping(path = "/tickets/{id}")
    Ticket updateTicket(@PathVariable int id, @RequestBody Ticket request){
        Ticket t = ticketRepository.findById(id);
        if(t == null) {return null;}
        t.setPrice(request.getPrice());
        t.setRow(request.getRow());
        t.setSection(request.getSection());
        t.setIs_active(request.isIs_active());

        ticketRepository.save(t);
        return ticketRepository.findById(id);
    }

    @DeleteMapping(path = "/tickets/{id}")
    String deleteTicket(@PathVariable int id){
        Ticket ticket = ticketRepository.findById(id);
        if(ticket == null)  return failure;
        ticketRepository.delete(ticket);
        return success;
    }



    @PutMapping("/tickets/assign")
    void assignTicket(@RequestParam int ticketId, @RequestParam int accountId){
        Account account = accountRepository.findById(accountId);
        Ticket ticket = ticketRepository.findById(ticketId);
        account.addTicket(ticket);
        ticket.setAccount(account);
        accountRepository.save(account);

    }

    @PutMapping("/ticket/assign_to_event/{eventId}/{ticketId}")
    void assignEvent(@RequestParam int eventId, @RequestParam int ticketId){
        Ticket ticket = ticketRepository.findById(ticketId);
        Event event = eventRepository.findById(eventId);
        event.addTicket(ticket);
        eventRepository.save(event);
        ticket.setEvent(event);
        ticketRepository.save(ticket);

    }
}
