package com.shapesynergy.dietworkout.exercises;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExercisesServlet {
    private final ExercisesService exerciseService;

    public ExercisesServlet(ExercisesService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/exercises")
    public String getExerciseFact() throws JsonProcessingException {
        return exerciseService.getExerciseName();

    }
}

