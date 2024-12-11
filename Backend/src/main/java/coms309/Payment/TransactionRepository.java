package coms309.Payment;

import coms309.Accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pablo Leguizamo
 *
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findById(int id);

    @Transactional
    void deleteById(int id);
}
