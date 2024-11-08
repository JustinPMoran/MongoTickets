package coms309.Friends;

import coms309.Accounts.Account;
import jakarta.persistence.*;

@Entity
//@Table(name = "friendships")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id_1")
    private Account account1;

    @ManyToOne
    @JoinColumn(name = "account_id_2")
    private Account account2;

    @Column(nullable = false)
    private String status; // e.g., 'pending', 'friends', 'blocked'

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount1() {
        return account1;
    }

    public void setAccount1(Account account1) {
        this.account1 = account1;
    }

    public Account getAccount2() {
        return account2;
    }

    public void setAccount2(Account account2) {
        this.account2 = account2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
