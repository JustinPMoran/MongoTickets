package coms309.LiveTickets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LiveRepository extends JpaRepository<Price, Integer> {
    Price findById(int id);

    @Transactional
    void deleteById(int id);
}
