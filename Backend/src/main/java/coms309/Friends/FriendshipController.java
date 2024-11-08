package coms309.Friends;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FriendshipController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;

    @GetMapping(path = "/get_friendships/{id}")
    public List<Account> getFriends(@PathVariable int id) {
        Account user = accountRepository.findById(id);
        List<Friendship> outgoingFriends = friendshipRepository.findByAccount1AndStatus(user, "friends");


        List<Friendship> incomingFriends = friendshipRepository.findByAccount2AndStatus(user, "friends");

        List<Account> allFriends = new ArrayList<>();
        outgoingFriends.forEach(f -> allFriends.add(f.getAccount2()));
        incomingFriends.forEach(f -> allFriends.add(f.getAccount1()));

        return allFriends;
    }

}
