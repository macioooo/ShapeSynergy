package com.shapesynergy.dietworkout.WorkoutPlans;

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
}
