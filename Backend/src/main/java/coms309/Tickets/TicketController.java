package coms309.Tickets;

import java.util.List;

import coms309.Accounts.Account;
import coms309.Accounts.AccountController;
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
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/tickets")
    List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    @GetMapping(path = "/tickets/{id}")
    Ticket getTicketById(@PathVariable int id){
        return ticketRepository.findById(id);
    }

    @PostMapping(path = "/tickets")
    String createTicket(@RequestBody Ticket Ticket){
        if (Ticket == null)
            return failure;
        ticketRepository.save(Ticket);
        return success;
    }

    @PutMapping(path = "/tickets/{id}")
    Ticket updateTicket(@PathVariable int id, @RequestBody Ticket request){
        Ticket ticket = ticketRepository.findById(id);
        if(ticket == null)
            return null;
        ticketRepository.save(request);
        return ticketRepository.findById(id);
    }

//    @DeleteMapping(path = "/tickets/{id}")
//    String deleteTicket(@PathVariable int id){
//
//        // Check if there is an object depending on User and then remove the dependency
//        Account account = accountRepository.findByTicket_Id(id);
//        account.setTicket(null);
//        accountRepository.save(account);
//
//        // delete the ticket if the changes have not been reflected by the above statement
//        ticketRepository.deleteById(id);
//        return success;
//    }


    @DeleteMapping(path = "/tickets/{id}")
    String deleteTicket(@PathVariable int id){
        Ticket ticket = ticketRepository.findById(id);
        if(ticket == null) { return failure; }
        ticketRepository.delete(ticket);
        return success;
    }



    @PutMapping("/ticket/assign")
    void assignTicket(@RequestParam int ticketId, @RequestParam int accountId){
        Account account = accountRepository.findById(accountId);
        Ticket ticket = ticketRepository.findById(ticketId);
        account.addTicket(ticket);
        ticket.setAccount(account);
        ticketRepository.save(ticket);
        accountRepository.save(account);

    }
}
