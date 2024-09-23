package coms309.Users;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findById(int id);

    void deleteById(int id);

    User findByTicket_Id(int id);
}
