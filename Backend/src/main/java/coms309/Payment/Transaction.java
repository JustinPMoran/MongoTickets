package coms309.Payment;

import coms309.Accounts.Account;
import coms309.Events.Event;
import coms309.Tickets.Ticket;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private Event event;

    private double amountPaid;
    private String paymentStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Transaction() {
    }

    public Transaction(Account account, Ticket ticket, Event event, double amountPaid, String paymentStatus) {
        this.account = account;
        this.ticket = ticket;
        this.event = event;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.createdDate = LocalDateTime.now(); // Set the creation timestamp
        this.updatedDate = LocalDateTime.now(); // Set the update timestamp
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
