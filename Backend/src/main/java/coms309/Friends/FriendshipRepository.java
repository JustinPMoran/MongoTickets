package coms309.Friends;

import coms309.Accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByAccount1AndStatus(Account account1, String status);
    List<Friendship> findByAccount2AndStatus(Account account2, String status);
}
