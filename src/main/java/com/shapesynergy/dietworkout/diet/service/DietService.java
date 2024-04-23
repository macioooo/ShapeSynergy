// DietService.java
package com.shapesynergy.dietworkout.diet.service;

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


    public ResponseEntity<String> searchFood(String food) throws Exception {

        String query = this.dietRepository.buildFoodsSearchUrl(food, 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(query, HttpMethod.POST, entity, String.class);

        return response;
    }
}
