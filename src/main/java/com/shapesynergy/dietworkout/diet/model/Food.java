package com.shapesynergy.dietworkout.diet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Food {
    @JsonProperty("food_id")
    private String foodId;

    @JsonProperty("food_description")
    private String foodDescription;

    @JsonProperty("food_name")
    private String foodName;

    @JsonProperty("food_type")
    private String foodType;

    @JsonProperty("food_url")
    private String foodUrl;

    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;

        private Double getMacrosOrCalories(String macroOrCalorieToSplit) {
            if (foodDescription != null) {
                int hyphenIndex = foodDescription.indexOf('-');
                String valuePart = foodDescription.substring(hyphenIndex + 1).trim();
                String[] parts = valuePart.split("\\|");
                for (String part : parts) {
                    if (part.contains(macroOrCalorieToSplit + ":")) {
                        String macroOrCalorie = part.trim().replace(macroOrCalorieToSplit + ":", "").trim();
                        return Double.parseDouble(macroOrCalorie.replaceAll("[^0-9.]", ""));
                    }
                }
            }
            return null;
        }

    public Double getCalories() {
        if (calories == null) {
            calories = getMacrosOrCalories("Calories");
        }
        return calories;
    }

    public Double getFat() {
        if (fat == null) {
            fat = getMacrosOrCalories("Fat");
        }
        return fat;
    }

    public Double getProtein() {
        if (protein == null) {
            protein = getMacrosOrCalories("Protein");
        }
        return protein;
    }

    public Double getCarbs() {
        if (carbs == null) {
            carbs = getMacrosOrCalories("Carbs");
        }
        return carbs;
    }
}