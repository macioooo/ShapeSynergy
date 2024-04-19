package com.shapesynergy.dietworkout.diet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapesynergy.dietworkout.diet.Food;
import com.shapesynergy.dietworkout.diet.service.DietService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/diet")
@AllArgsConstructor
public class DietRestController {
    private final DietService dietService;

    @PostMapping("/search")
    public ResponseEntity<Food> searchFoodByName() throws Exception {
        ResponseEntity<String> response = dietService.searchFoods();
        Food food = convertResponseToFood(response.getBody());
        return ResponseEntity.ok(food);
    }

    private Food convertResponseToFood(String responseBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, Food.class);
    }
}