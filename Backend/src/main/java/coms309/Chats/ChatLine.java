package coms309.Chats;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Accounts.Account;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class ChatLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private Chat chat;

    @ManyToOne
//    @JoinColumn(name = "sender_account_id")
    private Account sender_account;

    private String line_text;
    private Timestamp created_timestamp;

    public ChatLine(Chat chat, Account sender, String text){
        this.chat = chat;
        this.sender_account = sender;
        this.line_text = text;
        this.created_timestamp = new Timestamp(System.currentTimeMillis());

    }

    public ChatLine(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Account getSender_account() {
        return sender_account;
    }

    public void setSender_account(Account sender_account) {
        this.sender_account = sender_account;
    }

    public String getLine_text() {
        return line_text;
    }

    public void setLine_text(String line_text) {
        this.line_text = line_text;
    }

    public Timestamp getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(Timestamp created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    @Override
    public String toString(){

        return line_text;
    }




}
