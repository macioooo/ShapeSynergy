package com.shapesynergy.dietworkout.exercises;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/exercises",
        produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ExercisesController {
    private final ExercisesService exerciseService;

    @GetMapping("/search")
    public String searchExercise(@RequestParam String muscle, @RequestParam String exercise, @RequestParam int offset) throws JsonProcessingException {
        return exerciseService.searchForExercise(muscle, exercise, offset);
    }
    @PostMapping("/getarray")
    public ResponseEntity<String> getArrayWithExercises(@RequestBody ArrayList<String> exerciseList) {
        exerciseService.add(exerciseList);
        return ResponseEntity.status(HttpStatus.OK).body("Exercises added successfully");
    }

}

