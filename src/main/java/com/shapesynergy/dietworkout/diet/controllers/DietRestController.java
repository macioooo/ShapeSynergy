package com.shapesynergy.dietworkout.diet.controllers;
import com.shapesynergy.dietworkout.diet.DietDTO;
import com.shapesynergy.dietworkout.diet.service.DietService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<DietDTO> searchFoodByName(@RequestParam String food) {
        try {
            DietDTO dietDTO = dietService.searchFood(food);
            return ResponseEntity.ok(dietDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}