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

    @GetMapping(path = "/accounts")
    List<Account> getAllAccounts(){
        return AccountRepository.findAll();
    }

    @PostMapping(path = "/accounts")
    String createUsers(@RequestBody Account account){
        if (account == null)
            return failure;
        AccountRepository.save(account);
        return success;
    }

    @GetMapping(path = "/accounts/{id}")
    Account getUserById(@PathVariable int id){
        return AccountRepository.findById(id);
    }


    @PutMapping("/accounts/{id}")
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

    @DeleteMapping(path = "/accounts/{id}")
    String deleteUser(@PathVariable int id){
        AccountRepository.deleteById(id);
        return success;
    }

    @GetMapping("/accounts/{id}/tickets")
    List<Ticket> getUserTickets(@PathVariable int id){
        Account account = AccountRepository.findById(id);
        if (account == null) {throw new RuntimeException("User not found");}
        return account.getTickets();
    }

    @PostMapping ("/accounts/forgot_password")
    public void forgotPassword(@RequestParam String email, @RequestParam String pass) {
        Account user = AccountRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setPassword(pass);
        AccountRepository.save(user);
    }

    @PutMapping ("/accounts/change_password")
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

    @PutMapping("/accounts_change_username")
    void changeUsername(@RequestParam String email, @RequestParam String newUsername) {
        Account account = AccountRepository.findByEmail(email);
        if (account == null) {
            throw new RuntimeException("User not found");
        }
        account.setUsername(newUsername);
        AccountRepository.save(account);
    }

    @PostMapping ("/accounts/signup")
    String signUp(@RequestBody Account account) {
        String email = account.getEmail();
        String password = account.getPassword();
        String username = account.getUsername();

        if (email == null || password == null || username == null) {
            return "Invalid Input";
        }
        AccountRepository.save(account);
        return success;
    }

    @PostMapping("/accounts/login")
    public boolean loginByEmail(@RequestParam String email, @RequestParam String pass) {
        Account acc = AccountRepository.findByEmail(email);
        if (acc == null) { throw new RuntimeException("User not found"); }
        if (acc.getPassword().equals(pass)) {return true;}
        else { return false; }
    }




}
