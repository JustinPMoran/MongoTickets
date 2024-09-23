package coms309.Tickets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import coms309.Users.User;

/**
 * 
 * @author Pablo Leguizamo
 */ 

@Entity
public class Ticket {
    
    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String event_name;
    private String event_date;

    /*
     * @OneToOne creates a relation between the current entity/table(Ticket) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either User/Ticket objects (Ticket->User->Ticket->...)
     */
    @OneToOne
    @JsonIgnore
    private User user;

    public Ticket(String event_name, String event_date, String section, int row) {
        this.event_name = event_name;
        this.event_date = event_date;
        this.section = section;
        this.row = row;

    }

    public Ticket() {
    }

    // =============================== Getters and Setters for each field ================================== //

    // ID for every ticket returns ticket ID number
    public int getId(){
        return id;
    }

    // Sets the ticket ID
    public void setId(int id){
        this.id = id;
    }

    // Returns the name of the event
    public String getEvent_name() {
        return event_name;
    }

    // Sets the name of the event
    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    // returns the date of the event
    public String getEvent_date() {
        return event_date;
    }

    // sets the events date
    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    // gets the user
    public User getUser(){
        return user;
    }

    
    public void setUser(User User){
        this.user = User;
    }

    // sets the ticket price
    public void setPrice(int price) {
        this.price = price;
    }

    // gets the ticket price
    public int getPrice() {
        return price;
    }

    // Gets the ticket Section 
    public String getSection() {
        return section;
    }

    // Gets the ticket row
    public int getRow() {
        return row;
    }
}
