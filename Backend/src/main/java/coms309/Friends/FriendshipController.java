package coms309.Friends;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class FriendshipController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;

    @GetMapping(path = "/get_friendships/{id}")
    public List<Account> getFriends(@PathVariable int id) {
        Account user;
        try {
            user = accountRepository.findById(id);
            if (user == null) {
                throw new RuntimeException("Account not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Account retrieval failed: " + e.getMessage());
        }

        List<Friendship> outgoingFriends = friendshipRepository.findByAccount1AndStatus(user, "friends");
        List<Friendship> incomingFriends = friendshipRepository.findByAccount2AndStatus(user, "friends");

        List<Account> allFriends = new ArrayList<>();
        outgoingFriends.forEach(f -> allFriends.add(f.getAccount2()));
        incomingFriends.forEach(f -> allFriends.add(f.getAccount1()));

        return allFriends;
    }

    @PostMapping("/friendship/send-request")
    public String sendFriendRequest(@RequestParam int senderId, @RequestParam int receiverId) {
        Account sender;
        Account receiver;
        try {
            sender = accountRepository.findById(senderId);
            receiver = accountRepository.findById(receiverId);
            if (sender == null || receiver == null) {
                throw new RuntimeException("Sender or Receiver not found");
            }
        } catch (Exception e) {
            return "Account retrieval failed: " + e.getMessage();
        }

        // Check for existing friendship or pending request
        boolean existingFriendship = friendshipRepository.existsByAccount1IdAndAccount2Id(senderId, receiverId)
                || friendshipRepository.existsByAccount1IdAndAccount2Id(receiverId, senderId);
        if (existingFriendship) {
            return "Friend request already exists or users are already friends.";
        }

        Friendship friendship = new Friendship();
        friendship.setAccount1(sender);
        friendship.setAccount2(receiver);
        friendship.setStatus("pending");
        friendshipRepository.save(friendship);

        return "Friend request sent successfully.";
    }

    @PostMapping("/friendship/accept-request")
    public String acceptFriendRequest(@RequestParam int senderId, @RequestParam int receiverId) {
        Friendship friendship;
        try {
            friendship = friendshipRepository.findByAccount1IdAndAccount2Id(senderId, receiverId);
            if (friendship == null) {
                throw new RuntimeException("Friend request not found");
            }
        } catch (Exception e) {
            return "Friendship retrieval failed: " + e.getMessage();
        }

        friendship.setStatus("friends");
        friendshipRepository.save(friendship);

        return "Friend request accepted.";
    }

    @PostMapping("/friendship/reject-request")
    public String rejectFriendRequest(@RequestParam int senderId, @RequestParam int receiverId) {
        Friendship friendship;
        try {
            friendship = friendshipRepository.findByAccount1IdAndAccount2Id(senderId, receiverId);
            if (friendship == null) {
                throw new RuntimeException("Friend request not found");
            }
        } catch (Exception e) {
            return "Friendship retrieval failed: " + e.getMessage();
        }

        friendshipRepository.delete(friendship);

        return "Friend request rejected.";
    }

    @DeleteMapping("/friendship/remove")
    public String removeFriend(@RequestParam int accountId1, @RequestParam int accountId2) {
        Friendship friendship;
        try {
            friendship = friendshipRepository.findByAccount1IdAndAccount2Id(accountId1, accountId2);
            if (friendship == null) {
                throw new RuntimeException("Friendship not found");
            }
        } catch (Exception e) {
            return "Friendship retrieval failed: " + e.getMessage();
        }

        friendshipRepository.delete(friendship);

        return "Friend removed successfully.";
    }



}
