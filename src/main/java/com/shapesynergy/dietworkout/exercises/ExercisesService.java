package com.shapesynergy.dietworkout.exercises;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExercisesService {
    @Value("${api.keyexercise}")
    private static final String API_KEY;
    public String getExerciseDetails() {
        try {
            String apiUrl = "https://api.api-ninjas.com/v1/exercises?muscle=biceps";
            RestTemplate restTemplate = new RestTemplate();
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            String name = path("name").asText();
            String description = path("description").asText();
            String muscleGroup = path("muscle").asText();

            return "Exercise Name: " + name + "\nDescription: " + description + "\nMuscle Group: " + muscleGroup;
        } catch (Exception e) {
            e.printStackTrace();
            return "Błąd podczas pobierania danych z API.";
        }
    }
}
