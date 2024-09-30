package coms309.Users;

import java.util.List;

import coms309.Tickets.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import coms309.Tickets.TicketRepository;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

@RestController
public class UserController {

    @Autowired
    UserRepository UserRepository;

    @Autowired
    TicketRepository ticketRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return UserRepository.findAll();
    }

    @GetMapping(path = "/user/{id}")
    User getUserById(@PathVariable int id){
        return UserRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUsers(@RequestBody User User){
        if (User == null)
            return failure;
        UserRepository.save(User);
        return success;
    }

    @PutMapping("/user/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User User = UserRepository.findById(id);

        if(User == null) {
            throw new RuntimeException("User id does not exist");
        }
        else if (User.getId() != id){
            throw new RuntimeException("path variable id does not match user request id");
        }

        UserRepository.save(request);
        return UserRepository.findById(id);
    }

    @PutMapping("/Users/{userId}/tickets/{ticketId}")
    String assignTicketToUser(@PathVariable int userId,@PathVariable int ticketId){
        User user = UserRepository.findById(userId);
        Ticket ticket = ticketRepository.findById(ticketId);
        if(user == null || ticket == null)
            return failure;
        ticket.setUser(user);
        user.setTicket(ticket);
        UserRepository.save(user);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        UserRepository.deleteById(id);
        return success;
    }
}
