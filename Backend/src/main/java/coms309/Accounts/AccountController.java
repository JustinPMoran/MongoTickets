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
    AccountRepository accountRepository;

    @Autowired
    TicketRepository ticketRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/accounts")
    List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @PostMapping(path = "/accounts")
    String createUsers(@RequestBody Account account){
        if (account == null)
            return failure;
        accountRepository.save(account);
        return success;
    }

    @DeleteMapping(path = "/accounts")
    String deleteUsers(){
        accountRepository.deleteAll();
        return success;
    }

    @GetMapping(path = "/accounts/{id}")
    Account getUserById(@PathVariable int id){
        return accountRepository.findById(id);
    }


    @PutMapping("/accounts/{id}")
    String updateUser(@PathVariable int id, @RequestBody Account request){
        Account account = accountRepository.findById(id);

        if(account == null)
            return failure;
        else if (request.getId() != id)
            return failure;

        accountRepository.save(request);
        return success;
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
        Account acc = accountRepository.findById(id);
        acc.getFriends().clear();
        accountRepository.save(acc);
        accountRepository.delete(acc);
        return success;
    }

    @GetMapping("/accounts/{id}/tickets")
    List<Ticket> getUserTickets(@PathVariable int id){
        Account account = accountRepository.findById(id);
        if (account == null) {throw new RuntimeException("User not found");}
        return account.getTickets();
    }

    @PostMapping ("/accounts/forgot_password")
    public void forgotPassword(@RequestParam String email, @RequestParam String pass) {
        Account user = accountRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setPassword(pass);
        accountRepository.save(user);
    }

    @PutMapping ("/accounts/change_password")
    public void changePassword(@RequestParam String email, @RequestParam String pass, @RequestParam String newPass) {
        Account user = accountRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (user.getPassword().equals(pass)) { // logged in
            user.setPassword(newPass);
        }
        accountRepository.save(user);
    }

    @PutMapping("/accounts_change_username")
    void changeUsername(@RequestParam String email, @RequestParam String newUsername) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new RuntimeException("User not found");
        }
        account.setUsername(newUsername);
        accountRepository.save(account);
    }

    @PostMapping ("/accounts/signup")
    String signUp(@RequestBody Account account) {
        String email = account.getEmail();
        String password = account.getPassword();
        String username = account.getUsername();

        if (email == null || password == null || username == null) {
            return "Invalid Input";
        }
        accountRepository.save(account);
        return success;
    }

    @PostMapping("/accounts/login")
    String loginByEmail(@RequestParam String email, @RequestParam String password) {
        Account acc = accountRepository.findByEmail(email);
        if (acc == null) {return failure; }
        if (acc.getPassword().equals(password)) {
            return success;
        }
        return failure;
    }




}
