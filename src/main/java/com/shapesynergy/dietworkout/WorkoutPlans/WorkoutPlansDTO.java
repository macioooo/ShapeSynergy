package com.shapesynergy.dietworkout.workoutPlans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class WorkoutPlansDTO {
    private ArrayList<String> exerciseList;
    private String workoutPlanName;
    private Long id_workout_plan;

    public WorkoutPlansDTO(ArrayList<String> exerciseList, String workoutPlanName) {
        this.exerciseList = exerciseList;
        this.workoutPlanName = workoutPlanName;
    }

    public WorkoutPlansDTO() {
    }
    public WorkoutPlansDTO(ArrayList<String> exerciseList, Long id_workout_plan) {
        this.exerciseList = exerciseList;
        this.id_workout_plan = id_workout_plan;
    }

}

