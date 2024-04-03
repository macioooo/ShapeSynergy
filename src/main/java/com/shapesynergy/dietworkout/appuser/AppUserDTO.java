package com.shapesynergy.dietworkout.appuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private String name;

    private String email;

    private String password;

    private AppUserRole appUserRole;

    //All needed for BMI calculation and diet plan
    private AppUserGender appUserGender;
    private Double height;
    private Double weight;
    private Integer age;
    private AppUserActivityLevel activityLevel;
    private AppUserGoal goal;
    private Double bmi;
    private AppUserBmiCategories bmiCategories;
    private Integer userCalories;
    private Integer userProtein;
    private Integer userCarbs;
    private Integer userFat;


    public String getFormattedActivityLevel() {
        if (activityLevel == null) {
            return "";
        }
        return activityLevel.name().toLowerCase().replace("_", " ");
    }
    public String getFormattedGoal() {
        if (goal == null) {
            return "";
        }
        return goal.name().toLowerCase().replace("_", " ");
    }
    public String getFormattedBmiCategories() {
        if (bmiCategories == null) {
            return "";
        }
        return bmiCategories.name().toLowerCase();
    }

}
