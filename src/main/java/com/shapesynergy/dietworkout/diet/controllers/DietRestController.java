package com.shapesynergy.dietworkout.diet.controllers;
import com.shapesynergy.dietworkout.appuser.AppUserDTO;
import com.shapesynergy.dietworkout.appuser.service.AppUserService;
import com.shapesynergy.dietworkout.appuser.service.CustomAppUserDetails;
import com.shapesynergy.dietworkout.diet.DietDTO;
import com.shapesynergy.dietworkout.diet.model.Food;
import com.shapesynergy.dietworkout.diet.service.DietService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diet")
@AllArgsConstructor
public class DietRestController {
    private final DietService dietService;
    private final AppUserService userService;

    @PostMapping("/search")
    public ResponseEntity<DietDTO> searchFoodByName(@RequestParam String food) {
        try {
            DietDTO dietDTO = dietService.searchFood(food);
            return ResponseEntity.ok(dietDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public void addFood(@RequestBody Food food, @AuthenticationPrincipal CustomAppUserDetails userDetails) {
        AppUserDTO appUser = userService.getUserInfo(userDetails.getId());
        userService.addUserDish(appUser, food);
    }



}