package coms309.Payment;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import coms309.Events.EventRepository;
import coms309.Tickets.Ticket;
import coms309.Events.Event;
import coms309.Tickets.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class TransactionController {


    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EventRepository eventRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @PostMapping("/transactions")
    public String createTransaction(@RequestParam int accountId, @RequestParam int ticketId, @RequestParam int eventId, @RequestParam double amountPaid) {

        try{
            Account account = accountRepository.findById(accountId);
            Ticket ticket = ticketRepository.findById(ticketId);
            Event event = eventRepository.findById(eventId);


            Transaction transaction = new Transaction(account, ticket, event, amountPaid, "Success");
            transactionRepository.save(transaction);
            return success;
        }catch (Exception e){
            System.out.println(e);
            return failure;
        }


    }

    @GetMapping("/transactions/{id}")
    public Transaction getTransactionById(@PathVariable int id) {

        return transactionRepository.findById(id);
    }
}
