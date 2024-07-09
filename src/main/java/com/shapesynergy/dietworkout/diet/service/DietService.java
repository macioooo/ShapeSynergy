// DietService.java
package com.shapesynergy.dietworkout.diet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapesynergy.dietworkout.diet.DietDTO;
import com.shapesynergy.dietworkout.diet.DietRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class DietService {
    private DietRepository dietRepository;


    public DietDTO searchFood(String food) throws Exception {
        String query = this.dietRepository.buildFoodsSearchUrl(food, 1);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(query, HttpMethod.POST, entity, String.class);
        DietDTO dietDTO = convertJsonToDietDTO(response.getBody());

        return dietDTO;
    }

    public DietDTO convertJsonToDietDTO(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, DietDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }
}
