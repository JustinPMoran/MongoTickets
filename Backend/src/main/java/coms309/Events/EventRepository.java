package coms309.Events;

import coms309.Accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pablo Leguizamo
 *
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findById(int id);

    @Transactional
    void deleteById(int id);
}
