package coms309.Events;

import coms309.Accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Pablo Leguizamo
 *
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findById(int id);

    void deleteById(int id);
}
