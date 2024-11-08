package coms309.Accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Account findById(int id);
    @Transactional
    void deleteById(int id);

    Account findByEmail(String email);

//    Account findByTicket_Id(int id);
}
