package coms309.Tickets;

import java.util.List;

import coms309.Accounts.Account;
import coms309.Accounts.AccountController;
import coms309.Events.Event;
import coms309.Events.EventRepository;
import coms309.LiveTickets.LiveTickets;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import coms309.Accounts.AccountRepository;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
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




    @Operation(summary = "Find all tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found All Tickets",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error finding Tickets",
                    content = @Content) })
    @GetMapping(path = "/tickets")
    List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }



    @Operation(summary = "Create a Ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created new Ticket",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error creating ticket",
                    content = @Content) })
    @PostMapping(path = "/tickets/{eventId}")
    String createTicket(@RequestBody Ticket ticket, @PathVariable int eventId){
        Event event = eventRepository.findById(eventId);
        if (ticket == null)
            return failure;
        ticket.setEvent(eventRepository.findById(eventId));
        event.addTicket(ticket);
        eventRepository.save(event);
        LiveTickets.broadcast("", eventId);
        return success;
    }



    @Operation(summary = "Delete All Tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted All Tickets",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Deleting All Tickets",
                    content = @Content) })
    @DeleteMapping(path = "/tickets")
    String deleteAllTickets(){
        ticketRepository.deleteAll();
        return success;
    }



    @Operation(summary = "Find ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Ticket",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error finding Ticket",
                    content = @Content) })
    @GetMapping(path = "/tickets/{id}")
    Ticket getTicketById(@PathVariable int id){
        return ticketRepository.findById(id);
    }



    @Operation(summary = "Update A ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Ticket",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Updating Ticket",
                    content = @Content) })
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




    @Operation(summary = "Delete a Ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted ticket",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Deleting Ticket",
                    content = @Content) })
    @DeleteMapping(path = "/tickets/{id}")
    String deleteTicket(@PathVariable int id){
        Ticket ticket = ticketRepository.findById(id);
        if(ticket == null)  return failure;
        ticketRepository.delete(ticket);
        return success;
    }


    @Operation(summary = "Assign a Ticket to a Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assigned Ticket",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Assigning ticket",
                    content = @Content) })
    @PutMapping("/tickets/assign")
    String assignTicket(@RequestParam int ticketId, @RequestParam int accountId){
        Account account = accountRepository.findById(accountId);
        Ticket ticket = ticketRepository.findById(ticketId);
        account.addTicket(ticket);
        ticket.setAccount(account);
        ticket.setIs_active(false);
        accountRepository.save(account);
        ticketRepository.save(ticket);

        return success;
    }

    @Operation(summary = "Assign Ticket to an Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assigned ticket to an Event",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error Assigning Ticket to an Event",
                    content = @Content) })
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
