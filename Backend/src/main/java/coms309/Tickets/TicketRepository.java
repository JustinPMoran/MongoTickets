package coms309.Tickets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findById(int id);

    @Transactional
    void deleteById(int id);
}



//