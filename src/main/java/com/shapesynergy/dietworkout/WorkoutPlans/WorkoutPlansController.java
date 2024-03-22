package com.shapesynergy.dietworkout.WorkoutPlans;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.shapesynergy.dietworkout.appuser.AppUser;
import com.shapesynergy.dietworkout.appuser.AppUserRepository;
import com.shapesynergy.dietworkout.appuser.service.CustomAppUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/exercises",
        produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class WorkoutPlansController {
    private final WorkoutPlansService workoutPlansService;
    private final AppUserRepository appUserRepository;


    @GetMapping("/search")
    public ResponseEntity<String> searchExercise(@RequestParam String muscle, @RequestParam String exercise, @RequestParam int offset) throws JsonProcessingException {
        return ResponseEntity.ok().body(workoutPlansService.searchForExercise(muscle, exercise, offset));
    }

    @PostMapping("/getarray")
    public ResponseEntity<String> getArrayWithExercises(@RequestBody WorkoutPlansDTO workoutPlansDTO, @AuthenticationPrincipal CustomAppUserDetails userDetails, Model model) {
        AppUser user = appUserRepository.findByEmail(userDetails.getUsername());
        if (user.getWorkoutPlans().size() >= 3) {
            String errorMessage = "You can have only 3 workout plans";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            workoutPlansService.add(workoutPlansDTO.getExerciseList(), user, workoutPlansDTO.getWorkoutPlanName());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Exercises added successfully");
    }

    @PostMapping("/deleteWorkoutPlan/{workoutPlanId}")
    public ResponseEntity<String> deleteWorkoutPlan(@PathVariable Long workoutPlanId,
                                                    @AuthenticationPrincipal CustomAppUserDetails userDetails) {
        AppUser user = appUserRepository.findByEmail(userDetails.getUsername());
        workoutPlansService.deleteWorkoutPlan(workoutPlanId, user);
        return ResponseEntity.ok().body("Workout plan deleted successfully");
    }
}






