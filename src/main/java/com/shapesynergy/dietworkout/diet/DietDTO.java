package com.shapesynergy.dietworkout.diet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shapesynergy.dietworkout.diet.model.Food;
import com.shapesynergy.dietworkout.diet.model.Foods;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class DietDTO {
    private Foods foods;

}
