package com.shapesynergy.dietworkout.WorkoutPlans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.shapesynergy.dietworkout.appuser.AppUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class WorkoutPlansService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String URL = "https://api.api-ninjas.com/v1/exercises";
    private WorkoutPlansRepository workoutPlansRepository;

    public WorkoutPlansService(@Value("${api.keyExercises}") String apiKey, WorkoutPlansRepository workoutPlansRepository) {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.set("X-Api-Key", apiKey);
        this.headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        this.workoutPlansRepository = workoutPlansRepository;
    }

    private ResponseEntity<String> fetchExercisesFromApi(String muscle, String exercise, int offset) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url_with_params = URL + "?muscle=" + muscle + "&name=" + exercise + "&offset=" + offset;
        return restTemplate.exchange(url_with_params, HttpMethod.GET, entity, String.class);
    }

    public String searchForExercise(String muscle, String exercise, int offset) throws JsonProcessingException {

        ResponseEntity<String> response = fetchExercisesFromApi(muscle, exercise, offset);
        if (response.getStatusCode() == HttpStatus.OK) {
            return displayExercises(response);
        }
        return "Failed to fetch exercise names. Status: " + response.getStatusCode();
    }

    private String displayExercises(ResponseEntity<String> response) throws JsonProcessingException {
        //StringBuilder exercisesBuilder = new StringBuilder();
        JsonNode root = objectMapper.readTree(response.getBody());
        int total = 0;
        String exerciseName = "";
        for (JsonNode exerciseNode : root) {
            exerciseName += exerciseNode.get("name").asText() + "\n";
            total++;
        }

        return exerciseName;
    }

    public String add(ArrayList<String> exerciseList, AppUser appUser, String workoutPlanName) {
            StringBuilder workoutPlan = new StringBuilder();
            for (String exercise : exerciseList) {
                workoutPlan.append(exercise).append(", ");
            }
            WorkoutPlans newWorkoutPlan = new WorkoutPlans(workoutPlan.toString());
            newWorkoutPlan.setUser(appUser);
            newWorkoutPlan.setWorkout_plan_name(workoutPlanName);
            workoutPlansRepository.save(newWorkoutPlan);
            return "Workout Plan saved";
    }

    public String deleteWorkoutPlan(Long id, AppUser user) {
        WorkoutPlans workoutPlan = workoutPlansRepository.findById(id).get();
        if (workoutPlan.getUser().equals(user)) {
            workoutPlansRepository.delete(workoutPlan);
            return "Workout plan deleted";
        } else {
            return "You can't delete this workout plan";
        }
    }




}

