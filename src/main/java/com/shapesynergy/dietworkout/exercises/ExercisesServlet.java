package com.shapesynergy.dietworkout.exercises;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExercisesServlet {
    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/exercise")
    public ResponseEntity<String> getExerciseData() {
        String fact = exerciseService.getExerciseFact();
        return ResponseEntity.ok(fact);
    }
}
