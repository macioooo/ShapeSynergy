package com.shapesynergy.dietworkout.diet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) //ignoring properties that i dont need
public class Foods {
    @JsonProperty("food")
    private List<Food> foods;
}
