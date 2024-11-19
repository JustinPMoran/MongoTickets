package coms309.Events;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import coms309.Tickets.Ticket;
import coms309.Tickets.TicketRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all Events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found All Events",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error finding All Events",
                    content = @Content) })
    @GetMapping(path = "/events")
    List<Event> getAllEvents(){ return eventRepository.findAll();}




    @Operation(summary = "Create an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created Event",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Creating Event",
                    content = @Content) })
    @PostMapping(path = "/events")
    String createEvent(@RequestBody Event event){
        if (event == null)
            return failure;
        eventRepository.save(event);
        return success;
    }



    @Operation(summary = "Delete All Events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted All Events",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Deleting All Events",
                    content = @Content) })
    @DeleteMapping(path = "/events")
    String deleteEvents(){
        eventRepository.deleteAll();
        return success;
    }



    @Operation(summary = "Get Event by Event ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Event",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Event Does not Exist",
                    content = @Content) })
    @GetMapping(path = "/events/{id}")
    Event getEventById(@PathVariable int id){
        return eventRepository.findById(id);
    }




    @Operation(summary = "Find all tickets linked to an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found All Tickets",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error finding Tickets in Event",
                    content = @Content) })
    @GetMapping(path = "/events/{id}/all_tickets")
    List<Ticket> getAllTickets(@PathVariable int id){
        return eventRepository.findById(id).getTickets();
    }



    @Operation(summary = "Update an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event Updated",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Updating Events",
                    content = @Content) })
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



    @Operation(summary = "Add Ticket to an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added Ticket to Event",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error",
                    content = @Content) })
    @PutMapping("/events/{id}/{ticket_id}")
    String addTicket(@PathVariable int id, @PathVariable int ticket_id){
        Event event = eventRepository.findById(id);
        if(event == null)
            return failure;

        event.addTicket(ticketRepository.findById(ticket_id));

        eventRepository.save(event);

        return success;
    }



    @Operation(summary = "Delete Event by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted Event",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Deleting Event",
                    content = @Content) })
    @DeleteMapping(path = "/events/{id}")
    String deleteEvent(@PathVariable int id){
        eventRepository.deleteById(id);
        return success;
    }




    @Operation(summary = "Find all tickets linked to an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found All Tickets",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error finding Tickets in Event",
                    content = @Content) })
    @GetMapping("/Tickets_in_Event/{id}")
    List<Ticket> getTicketsInEvent(@PathVariable int id){
        return eventRepository.findById(id).getTickets();
    }






}
