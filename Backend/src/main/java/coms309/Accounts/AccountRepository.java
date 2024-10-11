package coms309.Accounts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Account findById(int id);

    void deleteById(int id);

//    Account findByTicket_Id(int id);
}
