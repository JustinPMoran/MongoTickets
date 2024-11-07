package coms309.Chats;

import coms309.Tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findById(int id);

    @Transactional
    void deleteById(int id);

    Chat findTopByOrderByIdDesc();
}
