package coms309.Events;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import coms309.Tickets.Ticket;
import coms309.Tickets.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author Pablo Leguizamo
 *
 */

@RestController
public class EventController {
    @Autowired
    AccountRepository AccountRepository;

    @Autowired
    TicketRepository TicketRepository;

    @Autowired
    EventRepository EventRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/events")
    List<Event> getAllEvents(){ return EventRepository.findAll();}


    @PostMapping(path = "/events")
    String createEvent(@RequestBody Event event){
        if (event == null)
            return failure;
        EventRepository.save(event);
        return success;
    }

    @GetMapping(path = "/events/{id}")
    Event getEventById(@PathVariable int id){
        return EventRepository.findById(id);
    }

    @PutMapping("/events/{id}")
    Event updateEvent(@PathVariable int id, @RequestBody Event request){
        Event event = EventRepository.findById(id);

        if(event == null) {
            throw new RuntimeException("Event id does not exist");
        }
        else if (event.getId() != id){
            throw new RuntimeException("path variable id does not match user request id");
        }

        EventRepository.save(request);
        return EventRepository.findById(id);
    }

    @DeleteMapping(path = "/events/{id}")
    String deleteEvent(@PathVariable int id){
        EventRepository.deleteById(id);
        return success;
    }










}
