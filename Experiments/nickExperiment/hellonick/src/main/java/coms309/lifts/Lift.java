package coms309.lifts;


import java.util.ArrayList;

/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Lift {


    private String date;
    private String split_day;
    private ArrayList<Exercise> exercises;

    public Lift(){
        
    }

    public Lift(String date, String split_day, ArrayList<Exercise> exercises){
        this.date = date;
        this.split_day = split_day;
        this.exercises = exercises;
    }

    // Getters and Setters are necessary for Springboot JSON serialization
    // Alternatively, making fields public or adding the @JsonProperty tag works as well
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSplit_day() {
        return split_day;
    }

    public void setSplit_day(String split_day) {
        this.split_day = split_day;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

}
