package coms309.Tickets;

import coms309.Accounts.Account;
import coms309.Events.Event;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "`row`")
    private String row;
    private String section;
    private double price;
    private boolean is_active;

    /*
     * @ManyToOne creates a relation between the current entity/table(Ticket) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either User/Ticket objects (Ticket->User->Ticket->...)
     */
    @ManyToOne
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JsonIgnore
    private Event event;


    public Ticket(String row, String section, double price, boolean is_active) {
        this.row = row;
        this.section = section;
        this.price = price;
        this.is_active = is_active;
    }

    public Ticket() {
    }

    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}