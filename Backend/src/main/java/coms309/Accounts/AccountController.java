package coms309.Accounts;

import java.util.List;

import coms309.Chats.Chat;
import coms309.Tickets.Ticket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.bind.annotation.*;

import coms309.Tickets.TicketRepository;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TicketRepository ticketRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the accounts",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) }),
            @ApiResponse(responseCode = "400", description = "Error finding accounts",
                content = @Content) })
    @GetMapping(path = "/accounts")
    List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posted the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) })
    })
    @PostMapping(path = "/accounts")
    String createUsers( @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account to create", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class),
                    examples = @ExampleObject(value = "{ \"username\": \"pnleg\", \"password\": \"myPass\", \"email\": \"pnleg@iastate.edu\" }")))
                        @RequestBody Account account){
        if (account == null)
            return failure;
        accountRepository.save(account);
        return success;
    }

    @Operation(summary = "Delete all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) })})
    @DeleteMapping(path = "/accounts")
    String deleteUsers(){
        accountRepository.deleteAll();
        return success;
    }
    @Operation(summary = "Get account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) }),
            @ApiResponse(responseCode = "404", description = "Error finding account",
                    content = @Content) })
    @GetMapping(path = "/accounts/{id}")
    Account getUserById(@Parameter(description = "id of account to be searched") @PathVariable int id){
        return accountRepository.findById(id);
    }

    @Operation(summary = "Update account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated account successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class),
                            examples = @ExampleObject(value = success)) })
    })
    @PutMapping("/accounts/{id}")
    String updateUser(@Parameter(description = "id of account to be updated") @PathVariable int id, @RequestBody Account request){
        Account account = accountRepository.findById(id);

        if(account == null)
            return failure;
        else if (request.getId() != id)
            return failure;

        accountRepository.save(request);
        return success;
    }

    // Fix delete with friends
    @Operation(summary = "Delete account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) }),
            @ApiResponse(responseCode = "404", description = "Error finding account",
                    content = @Content) })
    @DeleteMapping(path = "/accounts/{id}")
    String deleteUser(@Parameter(description = "id of account to be deleted") @PathVariable int id){
        Account acc = accountRepository.findById(id);
        acc.getFriends().clear();
        accountRepository.save(acc);
        accountRepository.delete(acc);
        return success;
    }

    @Operation(summary = "Get account by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) }),
            @ApiResponse(responseCode = "404", description = "Error finding account",
                    content = @Content) })
    @GetMapping(path = "/accounts/email")
    Account getAccountByEmail(@Parameter(description = "Email of account to be deleted") @RequestParam String email){
        return accountRepository.findByEmail(email);
    }

    @Operation(summary = "Get an account's tickets by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the tickets",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error finding tickets",
                    content = @Content) })
    @GetMapping("/accounts/{id}/tickets")
    List<Ticket> getUserTickets(@Parameter(description = "Id of account owning the tickets") @PathVariable int id){
        Account account = accountRepository.findById(id);
        if (account == null) {throw new RuntimeException("User not found");}
        return account.getTickets();
    }
    @Operation(summary = "Get an account's chats by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the chats",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Error finding chats",
                    content = @Content) })
    @GetMapping("/accounts/{id}/chats")
    List<Chat> getUserChats(@Parameter(description = "Id of account that is a member of the chats") @PathVariable int id){
        Account account = accountRepository.findById(id);
        if (account == null) {throw new RuntimeException("User not found");}
        return account.getChats();
    }

    @Operation(summary = "Reset an account's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset",
                    content = { @Content(mediaType = "application/json") })})

    @PostMapping ("/accounts/forgot_password")
    public void forgotPassword(@Parameter(description = "Email of account to reset") @RequestParam String email, @Parameter(description = "New password for account") @RequestParam String pass) {
        Account user = accountRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setPassword(pass);
        accountRepository.save(user);
    }
    @Operation(summary = "Change an account's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed",
                    content = { @Content(mediaType = "application/json") })})

    @PutMapping ("/accounts/change_password")
    public void changePassword(@Parameter(description = "Email of account to change password") @RequestParam String email, @Parameter(description = "Old / current password") @RequestParam String pass, @Parameter(description = "New password for account") @RequestParam String newPass) {
        Account user = accountRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (user.getPassword().equals(pass)) { // logged in
            user.setPassword(newPass);
        }
        accountRepository.save(user);
    }

    @Operation(summary = "Change an account's username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username changed",
                    content = { @Content(mediaType = "application/json") })})

    @PutMapping("/accounts_change_username")
    void changeUsername(@Parameter(description = "Email of account to change username") @RequestParam String email, @Parameter(description = "New username for user") @RequestParam String newUsername) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new RuntimeException("User not found");
        }
        account.setUsername(newUsername);
        accountRepository.save(account);
    }

    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posted the account",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) })
    })
    @PostMapping ("/accounts/signup")
    String signUp(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account to create", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class),
                    examples = @ExampleObject(value = "{ \"username\": \"pnleg\", \"password\": \"myPass\", \"email\": \"pnleg@iastate.edu\" }")))
                  @RequestBody Account account) {
        String email = account.getEmail();
        String password = account.getPassword();
        String username = account.getUsername();

        if (email == null || password == null || username == null) {
            return "Invalid Input";
        }
        accountRepository.save(account);
        return success;
    }

    @Operation(summary = "Login account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account logged in",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) })
    })
    @PostMapping("/accounts/login")
    int loginByEmail(@Parameter(description = "Email of account to login") @RequestParam String email, @Parameter(description = "Password of account to login") @RequestParam String password) {
        Account acc = accountRepository.findByEmail(email);
        if (acc == null) {return 0; }
        if (acc.getPassword().equals(password)) {
            return acc.getId();
        }
        return 0;
    }

@PutMapping("/add_to_cart")
    String addToCart(@RequestParam int ticketID, @RequestParam int accountID) {
        Ticket ticket = ticketRepository.findById(ticketID);
        if (ticket == null) {
            return failure;
        }
        if (ticket.getAccount() == null) {
            Account account = accountRepository.findById(accountID);
            account.addMyCart(ticketRepository.findById(ticketID));
            accountRepository.save(account);
            return success;

        }
        else {
            return failure;
        }
}

@PutMapping("/remove_from_cart")
    String removeFromCart(@RequestParam int ticketID, @RequestParam int accountID) {
        Ticket ticket = ticketRepository.findById(ticketID);
        if (ticket == null || ticket.getAccount() != null) {
            return failure;
        }
        Account account = accountRepository.findById(accountID);
        account.removeMyCart(ticketRepository.findById(ticketID));
    accountRepository.save(account);
    return success;
}

@GetMapping("/get_cart")
    List<Ticket> getMyCart(@RequestParam int accountID) {
        Account account = accountRepository.findById(accountID);
        if (account == null) {
            return null;
        }
        return account.getMyCart();
}

@GetMapping("/my_tickets")
    List<Ticket> getMyTickets(@RequestParam int accountID) {
        Account account = accountRepository.findById(accountID);
        if (account == null) {
            return null;
        }
        return account.getTickets();
}


}
