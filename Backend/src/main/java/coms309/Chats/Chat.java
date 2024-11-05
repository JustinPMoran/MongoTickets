package coms309.Chats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Accounts.Account;
import coms309.Tickets.Ticket;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatLine> lines;
    @ManyToOne
    @JsonIgnore
    private Account account;

    // joinColumns specifies the FK column in the join table referencing the PK of the owning side (aka Chat)
    // inverseJoinColumns specifies the FK column in the join table referencing the PK of the OTHER side (aka Account)
    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "ChatAccount",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> members;

}
