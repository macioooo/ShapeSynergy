package com.shapesynergy.dietworkout.exercises;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExercisesService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String URL = "https://api.api-ninjas.com/v1/exercises";
    private int offset = 0;
    private int offsetTemp = 0;
    private String exerciseNames = "";

    public ExercisesService(@Value("${api.keyExercises}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.set("X-Api-Key", apiKey);
        this.headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    //Get exercises by muscle (and optionally by name)
    public String searchForExercise(String muscle, String exercise) throws JsonProcessingException {
        // Początkowe wyszukiwanie ćwiczeń
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url_with_params = URL + "?muscle=" + muscle + "&name=" + exercise + "&offset=" + offset;
        ResponseEntity<String> response = restTemplate.exchange(url_with_params, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return fetchAllExercises(response, muscle, exercise); // Przekazujemy muscle i exercise do metody fetchAllExercises
        } else {
            return "Failed to fetch exercise names. Status: " + response.getStatusCode();
        }
    }

    private String fetchAllExercises(ResponseEntity<String> response, String muscle, String exercise) throws JsonProcessingException {
        // Pobranie wszystkich ćwiczeń z odpowiedzi
        StringBuilder exercisesBuilder = new StringBuilder();
        int limit = 10; // Liczba wyników do pobrania w jednym zapytaniu
        int offset = 0;

        while (true) {
            JsonNode root = objectMapper.readTree(response.getBody());
            if (root.size() == 0) {
                // Nie ma więcej wyników, więc przerywamy pętlę
                break;
            }
            for (JsonNode exerciseNode : root) {
                String exerciseName = exerciseNode.get("name").asText();
                exercisesBuilder.append(exerciseName).append("<br>"); // Append each exercise name with system-dependent line separator
            }
            // Przesuwamy offset, aby pobrać następne wyniki
            offset += limit + 1;

            // Wykonujemy kolejne zapytanie
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String url_with_params = URL + "?muscle=" + muscle + "&name=" + exercise + "&offset=" + offset;
            response = restTemplate.exchange(url_with_params, HttpMethod.GET, entity, String.class);
        }

        return exercisesBuilder.toString();
    }
}
