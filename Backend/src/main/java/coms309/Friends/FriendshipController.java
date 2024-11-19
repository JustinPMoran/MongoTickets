package coms309.Friends;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all friends of a user by their account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved friends",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(path = "/get_friendships/{id}")
    public List<Account> getFriends(@Parameter(description = "id of account with friendships") @PathVariable int id) {
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

    @Operation(summary = "Send a friend request from one user to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid account IDs or request already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/friendship/send-request")
    public String sendFriendRequest(@Parameter(description = "id of sender account") @RequestParam int senderId, @Parameter(description = "id of receiver account") @RequestParam int receiverId) {
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

    @Operation(summary = "Accept a friend request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request accepted successfully"),
            @ApiResponse(responseCode = "404", description = "Friend request not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/friendship/accept-request")
    public String acceptFriendRequest(@Parameter(description = "id of sender account") @RequestParam int senderId, @Parameter(description = "id of receiver account") @RequestParam int receiverId) {
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

    @Operation(summary = "Reject a friend request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend request rejected successfully"),
            @ApiResponse(responseCode = "404", description = "Friend request not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/friendship/reject-request")
    public String rejectFriendRequest(@Parameter(description = "id of sender account") @RequestParam int senderId, @Parameter(description = "id of receiver account") @RequestParam int receiverId) {
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


    @Operation(summary = "Remove a friend connection between two users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully"),
            @ApiResponse(responseCode = "404", description = "Friendship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/friendship/remove")
    public String removeFriend(@Parameter(description = "id of 1st account") @RequestParam int accountId1,@Parameter(description = "id of 2nd account") @RequestParam int accountId2) {
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
