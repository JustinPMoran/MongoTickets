package coms309.Friends;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class FriendController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping(path = "/get_friends/{id}")
    public List<Account> getFriends(@PathVariable int id) {
        Account user = accountRepository.findById(id);

        return user.getFriends();
    }
    @PutMapping(path = "/create_friendship/{id1}/{id2}")
    public Account addFriend(@PathVariable int id1, @PathVariable int id2) {
        Account user = accountRepository.findById(id1);
        Account friend = accountRepository.findById(id2);

        user.getFriends().add(friend);
        friend.getFriends().add(user);

        accountRepository.save(user);
        accountRepository.save(friend);
        return user;
    }

    @DeleteMapping(path = "/remove_friendship/{id1}/{id2}")
    public void removeFriend(@PathVariable int id1, @PathVariable int id2) {
        Account user = accountRepository.findById(id1);
        Account friend = accountRepository.findById(id2);

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        accountRepository.save(user);
        accountRepository.save(friend);

    }


}
