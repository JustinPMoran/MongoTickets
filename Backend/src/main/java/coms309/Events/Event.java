package coms309.Events;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 *
 * @author Pablo Leguizamo
 */

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String date;
    private String location;
    private String description;
    private String max_capacity;


    public Event(String name, String date, String location, String description, String max_capacity) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.max_capacity = max_capacity;
    }

    public Event() {

    }


    // =============================== Getters and Setters for each field ================================== //


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(String max_capacity) {
        this.max_capacity = max_capacity;
    }
}
