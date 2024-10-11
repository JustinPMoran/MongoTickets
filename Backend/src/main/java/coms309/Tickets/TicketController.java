package coms309.Tickets;

import java.util.List;

import coms309.Accounts.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
