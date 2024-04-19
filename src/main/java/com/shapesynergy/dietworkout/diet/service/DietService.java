package com.shapesynergy.dietworkout.diet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DietService {
    private final DietAuthentication dietAuthentication;
    private final static String BASE_URL = "https://platform.fatsecret.com/rest/server.api";

    public DietService(DietAuthentication dietAuthentication) {
        this.dietAuthentication = dietAuthentication;
    }

    public ResponseEntity<String> searchFoods() throws Exception {
        String url = BASE_URL + "?method=foods.search&search_expression=corn&format=json&page_number=0&max_results=10";
        return dietAuthentication.makeRequest("POST", url);
    }
}
