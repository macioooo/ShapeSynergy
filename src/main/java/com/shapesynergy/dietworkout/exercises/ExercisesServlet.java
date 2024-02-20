package com.shapesynergy.dietworkout.exercises;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exercises")
public class ExercisesServlet {
    private final ExercisesService exerciseService;

    public ExercisesServlet(ExercisesService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/search")
    public String searchExercise(@RequestParam String muscle, @RequestParam String exercise, @RequestParam int offset) throws JsonProcessingException {
        return exerciseService.searchForExercise(muscle, exercise, offset);
    }
}

