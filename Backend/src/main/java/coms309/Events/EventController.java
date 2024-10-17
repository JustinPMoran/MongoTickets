package coms309.Events;

import coms309.Accounts.AccountRepository;
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
    AccountRepository accountRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EventRepository eventRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/events")
    List<Event> getAllEvents(){ return eventRepository.findAll();}


    @PostMapping(path = "/events")
    String createEvent(@RequestBody Event event){
        if (event == null)
            return failure;
        eventRepository.save(event);
        return success;
    }

    @DeleteMapping(path = "/events")
    String deleteUsers(){
        eventRepository.deleteAll();
        return success;
    }

    @GetMapping(path = "/events/{id}")
    Event getEventById(@PathVariable int id){
        return eventRepository.findById(id);
    }

    @PutMapping("/events/{id}")
    String updateEvent(@PathVariable int id, @RequestBody Event request){
        Event event = eventRepository.findById(id);
        if(event == null)
            return failure;
        else if (request.getId() != id)
            return failure;

        eventRepository.save(request);
        return success;
    }

    @DeleteMapping(path = "/events/{id}")
    String deleteEvent(@PathVariable int id){
        eventRepository.deleteById(id);
        return success;
    }










}
