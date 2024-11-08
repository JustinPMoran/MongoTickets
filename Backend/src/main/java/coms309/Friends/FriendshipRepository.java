package coms309.Friends;

import coms309.Accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByAccount1AndStatus(Account account1, String status);
    List<Friendship> findByAccount2AndStatus(Account account2, String status);

    Friendship findByAccount1IdAndAccount2Id(int accountId1, int accountId2);

    Friendship findByAccount1IdAndAccount2IdOrAccount1IdAndAccount2Id(int accountId1, int accountId2, int accountId2Alt, int accountId1Alt);

    boolean existsByAccount1IdAndAccount2Id(int accountId1, int accountId2);

    List<Friendship> findByAccount1IdAndStatus(int accountId, String status);

    List<Friendship> findByAccount2IdAndStatus(int accountId, String status);

}
