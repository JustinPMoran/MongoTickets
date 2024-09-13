package coms309.lifts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Set {
    public String reps;

    @JsonProperty
    private String weight;

    public Set(String reps, String weight){
        this.reps = reps;
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
