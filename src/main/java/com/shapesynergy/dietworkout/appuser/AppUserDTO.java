package com.shapesynergy.dietworkout.appuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppUserDTO {
    private String name;

    private String email;

    private String password;

    private AppUserRole appUserRole;

    //All needed for BMI calculation and diet plan
    private AppUserGender appUserGender;
    private Integer height;
    private Float weight;
    private Integer age;
    private Integer activityLevel;
    private Integer goal;
    private Integer bmi;



}
