package coms309.Chats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import coms309.Accounts.Account;
import coms309.Tickets.Ticket;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ChatLine> lines;

    // joinColumns specifies the FK column in the join table referencing the PK of the owning side (aka Chat)
    // inverseJoinColumns specifies the FK column in the join table referencing the PK of the OTHER side (aka Account)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ChatAccount",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> members;


    public Chat(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Account> getMembers() {
        return members;
    }

    public void setMembers(List<Account> members) {
        this.members = members;
    }

    public void addMember(Account member){
        this.members.add(member);
    }

    public List<ChatLine> getLines() {
        return lines;
    }

    public void setLines(List<ChatLine> lines) {
        this.lines = lines;
    }

    public void addLine(ChatLine line){
        this.lines.add(line);
    }

}
