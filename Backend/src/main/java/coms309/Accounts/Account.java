package coms309.Accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Chats.Chat;
import coms309.Friends.Friendship;
import jakarta.persistence.*;

import coms309.Tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Pablo Leguizamo
 * 
 */ 

@Entity
//@Table(name = "\"user\"")
public class Account {

     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String join_date;
    private boolean is_active;
    private int store_rating;
    private int store_review_count;
    private int events_visited_count;

    /*
     * @OneToOne creates a relation between the current entity/table(Ticket) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to Ticket within a User object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the User table will have a field called ticket_id
     */
    // MappedBy account means that the Ticket table has an account_id field that manages the relationship. This relationship is owned by the Ticket
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
//    @JoinColumn(name = "tickets_id")
    private List<Ticket> tickets;



    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "friendships",
            joinColumns = @JoinColumn(name = "account_id_1"),
            inverseJoinColumns = @JoinColumn(name = "account_id_2")
    )
    private List<Account> friends;

    @OneToMany(mappedBy = "account1")
    @JsonIgnore
    private List<Friendship> outgoingFriendships;

    @OneToMany(mappedBy = "account2")
    @JsonIgnore
    private List<Friendship> incomingFriendships;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private List<Chat> chats;



    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.tickets = new ArrayList<>();
    }

    public Account() {
    }

    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public int getStore_rating() {
        return store_rating;
    }

    public void setStore_rating(int store_rating) {
        this.store_rating = store_rating;
    }

    public int getStore_review_count() {
        return store_review_count;
    }

    public void setStore_review_count(int store_review_count) {
        this.store_review_count = store_review_count;
    }

    public int getEvents_visited_count() {
        return events_visited_count;
    }

    public void setEvents_visited_count(int events_visited_count) {
        this.events_visited_count = events_visited_count;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    public void addTicket(Ticket ticket){
        this.tickets.add(ticket);
    }

    public List<Account> getFriends() {
        return friends;
    }

    public void setFriends(List<Account> friends) {
        this.friends = friends;
    }
    public List<Friendship> getOutgoingFriendships() {
        return outgoingFriendships;
    }

    public void setOutgoingFriendships(List<Friendship> outgoingFriendships) {
        this.outgoingFriendships = outgoingFriendships;
    }

    public void addOutgoingFriendship(Friendship friendship){
        this.outgoingFriendships.add(friendship);
    }

    public List<Friendship> getIncomingFriendships() {
        return incomingFriendships;
    }

    public void setIncomingFriendships(List<Friendship> incomingFriendships) {
        this.incomingFriendships = incomingFriendships;
    }

    public void addIncomingFriendship(Friendship friendship){
        this.incomingFriendships.add(friendship);
    }


    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public void addChat(Chat chat) {
        this.chats.add(chat);
    }
}
