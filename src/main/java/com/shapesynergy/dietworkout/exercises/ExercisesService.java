package com.shapesynergy.dietworkout.exercises;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class ExercisesService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String URL = "https://api.api-ninjas.com/v1/exercises";
    private final int LIMIT = 10; // Limit wyników do pobrania w jednym zapytaniu

    public ExercisesService(@Value("${api.keyExercises}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.set("X-Api-Key", apiKey);
        this.headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    private ResponseEntity<String> fetchExercisesFromApi(String muscle, String exercise, int offset) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url_with_params = URL + "?muscle=" + muscle + "&name=" + exercise + "&offset=" + offset;
        return restTemplate.exchange(url_with_params, HttpMethod.GET, entity, String.class);
    }

    public String searchForExercise(String muscle, String exercise) throws JsonProcessingException {
        int offset = 0;
        StringBuilder exercisesBuilder = new StringBuilder();

        while (true) {
            ResponseEntity<String> response = fetchExercisesFromApi(muscle, exercise, offset);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                if (root.size() == 0) {
                    break; // Przerwanie pętli, gdy nie ma więcej wyników
                }
                for (JsonNode exerciseNode : root) {
                    String exerciseName = exerciseNode.get("name").asText();
                    exercisesBuilder.append(exerciseName).append("<br>");
                }
                offset += LIMIT; // Aktualizacja offsetu
            } else {
                return "Failed to fetch exercise names. Status: " + response.getStatusCode();
            }
        }

        return exercisesBuilder.toString();
    }
}
