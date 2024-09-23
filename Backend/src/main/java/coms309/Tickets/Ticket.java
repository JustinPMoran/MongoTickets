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

    public Ticket(String event_name, String event_date) {
        this.event_name = event_name;
        this.event_date = event_date;

    }

    public Ticket() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User User){
        this.user = User;
    }

}
