package com.shapesynergy.dietworkout.diet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DietService {
    private final static String BASE_URL = "https://platform.fatsecret.com/rest/server.api";
    private static String CONSUMER_SECRET;
    private static String CONSUMER_KEY;

    public DietService(@Value("${fatsecret.api.consumerKey}") String CONSUMER_KEY,
                       @Value("${fatsecret.api.consumerSecret}") String CONSUMER_SECRET) {
        this.CONSUMER_KEY = CONSUMER_KEY;
        this.CONSUMER_SECRET = CONSUMER_SECRET;
    }

    public ResponseEntity<String> searchFood() {
        // Construct OAuth authorization header
        //String authorizationHeader =

        String url = BASE_URL + "?method=foods.search"
                + "&search_expression=corn"
                + "&format=json"
                + "&page_number=0"
                + "&max_results=10";

        // Create headers with OAuth authorization
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",  new DietAuthentication()
                .withParameter("oauth_signature_method", "HMAC-SHA1")
                .withMethod("POST")
                .withURL(BASE_URL)
                .withConsumerSecret(CONSUMER_SECRET)
                .withParameter("oauth_consumer_key", CONSUMER_KEY)
                .build());


        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response;
    }
}
