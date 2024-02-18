package com.shapesynergy.dietworkout.exercises;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.Optional;

@Service
public class ExercisesService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String URL = "https://api.api-ninjas.com/v1/exercises";
    @Value("${api.keyExercises}")
    private String apiKey;

//API key need to be in header for the request
    private HttpHeaders headerSetup() {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Api-Key", apiKey);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            return headers;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch API key");
        }

    }
    public String getExerciseName() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headerSetup());
        ResponseEntity<String> response =
                restTemplate.
                        exchange(URL + "?/muscle=chest", HttpMethod.GET, entity, String.class);
        try {
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                StringBuilder exerciseNames = new StringBuilder();
                int i = 0;
                for (JsonNode exercise : root) {
                    if (i == 2) {
                        break;
                    }
                    String exerciseName = exercise.get("name").asText();
                    exerciseNames.append(exerciseName).append("\n");
                    i++;
                }

                return exerciseNames.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed to fetch exercise name";
    }


}
