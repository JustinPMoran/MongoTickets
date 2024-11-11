package coms309.LiveTickets;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int eventId;
    private double oldPrice;
    private double newPrice;
    private Timestamp changeTime;

    // Constructors
    public Price() {}

    public Price(int eventId, double oldPrice, double newPrice) {
        this.eventId = eventId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.changeTime = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }
}
