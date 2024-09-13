package coms309.lifts;

import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Vivek Bengre
 */

// Count lifts
// Filter by date / lift type
// Calculate 1RM
@RestController
public class FitnessController {

    ArrayList<Lift> liftList = new ArrayList<>();

    @GetMapping("/lifts")
    public  ArrayList<Lift>  getAllLifts() {
        return liftList;
    }

    @PostMapping("/lifts")
    public  String createLift(@RequestBody ArrayList<Lift> lifts) {
        liftList.addAll(lifts);

        return lifts.get(0).getDate();
    }

    @GetMapping("/lift_count")
    public int getCount(){
        return liftList.size();
    }

    @GetMapping("/lifts/split_day")
    public ArrayList<Lift> filterSplitDay(@RequestParam("split_day") String split_day){
        ArrayList<Lift> res = new ArrayList<Lift>();
        for (Lift lift : liftList ){
            if (lift.getSplit_day().contains(split_day)){
                res.add(lift);
            }
        }
        return res;
    }

    @GetMapping("/lifts/month")
    public ArrayList<Lift> filterMonth(@RequestParam("month") String month){
        ArrayList<Lift> res = new ArrayList<Lift>();
        for (Lift lift : liftList ){
            if (lift.getDate().contains(month)){
                res.add(lift);
            }
        }
        return res;
    }

    @GetMapping("/lifts/{date}/{exercise_name}/one_rep_max")
    public String calculate1RM(@PathVariable String date, @PathVariable String exercise_name){
        for (Lift lift : liftList ){
            if (lift.getDate().equals(date)){
                for (Exercise e : lift.getExercises()){
                    if (e.name.equals(exercise_name)){
                        double orm;
                        double avgorm = 0;
                        for (Set s : e.sets){
                            orm = Float.parseFloat(s.getWeight()) / (1.0278 - 0.0278 * Float.parseFloat(s.reps));
                            avgorm += orm;
                        }
                        orm = avgorm / e.sets.size();
                        return "Estimated one rep max for " + exercise_name + " on " + date + " is " + orm;
                    }
                }
            }
        }
        return "res";
    }

    @DeleteMapping("/lifts/{date}")
    public ArrayList<Lift> deleteLift(@PathVariable String date) {
        Lift res = null;
        for (Lift lift : liftList){
            if (lift.getDate().equals(date)){
                res = lift;
            }
        }
        if (res != null)
            liftList.remove(res);

        return liftList;
    }
    @DeleteMapping("/lifts/clear")
    public ArrayList<Lift> clearLifts() {
        liftList = new ArrayList<>();

        return liftList;
    }




}

