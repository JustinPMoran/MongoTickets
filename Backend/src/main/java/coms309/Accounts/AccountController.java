package coms309.Accounts;

import java.util.List;

import coms309.Tickets.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import coms309.Tickets.TicketRepository;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

@RestController
public class AccountController {

    @Autowired
    AccountRepository AccountRepository;

    @Autowired
    TicketRepository TicketRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<Account> getAllUsers(){
        return AccountRepository.findAll();
    }

    @GetMapping(path = "/user/{id}")
    Account getUserById(@PathVariable int id){
        return AccountRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUsers(@RequestBody Account Account){
        if (Account == null)
            return failure;
        AccountRepository.save(Account);
        return success;
    }

    @PutMapping("/user/{id}")
    Account updateUser(@PathVariable int id, @RequestBody Account request){
        Account Account = AccountRepository.findById(id);

        if(Account == null) {
            throw new RuntimeException("User id does not exist");
        }
        else if (Account.getId() != id){
            throw new RuntimeException("path variable id does not match user request id");
        }

        AccountRepository.save(request);
        return AccountRepository.findById(id);
    }

//    @PutMapping("/Users/{userId}/tickets/{ticketId}")
//    String assignTicketToUser(@PathVariable int userId,@PathVariable int ticketId){
//        Account account = AccountRepository.findById(userId);
//        Ticket ticket = ticketRepository.findById(ticketId);
//        if(account == null || ticket == null)
//            return failure;
//        ticket.setUser(account);
//        account.setTicket(ticket);
//        AccountRepository.save(account);
//        return success;
//    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        AccountRepository.deleteById(id);
        return success;
    }



    @GetMapping("/user/{id}/tickets")
    List<Ticket> getUserTickets(@PathVariable int id){
        Account account = AccountRepository.findById(id);
        if (account == null) {throw new RuntimeException("User not found");}
        return account.getTickets();
    }



    @PostMapping("/user/login")
    public boolean loginByEmail(@RequestParam String email, @RequestParam String pass) {
        Account acc = AccountRepository.findByEmail(email);
        if (acc == null) { throw new RuntimeException("User not found"); }
        if (acc.getPassword().equals(pass)) {return true;}
        else { return false; }
    }

    @PostMapping ("/user/forgot/password")
    public void forgotPassword(@RequestParam String email, @RequestParam String pass) {
        Account user = AccountRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setPassword(pass);
        AccountRepository.save(user);
    }

    @PutMapping ("/user/change/password")
    public void changePassword(@RequestParam String email, @RequestParam String pass, @RequestParam String newPass) {
        Account user = AccountRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (user.getPassword().equals(pass)) { // logged in
            user.setPassword(newPass);
        }
        AccountRepository.save(user);
    }

}
