// DietService.java
package com.shapesynergy.dietworkout.diet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class DietService {
    private final String BASE_URL = "https://platform.fatsecret.com/rest/server.api";
    private final String CONSUMER_SECRET;
    private DietAuthentication dietAuthentication;
    private final String CONSUMER_KEY;

    public DietService(@Value("${fatsecret.api.consumerKey}") String consumerKey,
                       @Value("${fatsecret.api.consumerSecret}") String consumerSecret) {
        this.CONSUMER_KEY = consumerKey;
        this.CONSUMER_SECRET = consumerSecret;
        this.dietAuthentication = new DietAuthentication(CONSUMER_KEY, CONSUMER_SECRET);
    }

    public ResponseEntity<String> searchFood(String food) throws Exception {

        String query = this.dietAuthentication.buildRecipesSearchUrl(food, 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(query, HttpMethod.POST, entity, String.class);

        return response;
    }
}
