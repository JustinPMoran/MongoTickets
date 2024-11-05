package coms309.Chats;

import coms309.Accounts.Account;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class ChatLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
//    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
//    @JoinColumn(name = "sender_account_id")
    private Account sender_account;

    private String line_text;
    private Timestamp created_timestamp;


}
