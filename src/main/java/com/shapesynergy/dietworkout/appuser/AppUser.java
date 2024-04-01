package com.shapesynergy.dietworkout.appuser;

import com.shapesynergy.dietworkout.WorkoutPlans.WorkoutPlans;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//Implements user role, access certain endpoints, APIs etc.
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "`user`")
public class AppUser {

    @Id
    @GeneratedValue
    private Long id_user;
    @Column(nullable = false)
    private String name;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<WorkoutPlans> workoutPlans;

    //All needed for BMI calculation and diet plan
    @Enumerated(EnumType.STRING)
    private AppUserGender appUserGender;
    private Double height;
    private Double weight;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private AppUserActivityLevel activityLevel;
    @Enumerated(EnumType.STRING)
    private AppUserGoal goal;
    private Double bmi;
    @Enumerated(EnumType.STRING)
    private AppUserBmiCategories bmiCategories;

    //Spring security
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    public AppUser(String name, String email, String password,
                   AppUserGender appUserGender, Double height, Double weight, Integer age,
                   AppUserActivityLevel activityLevel, AppUserGoal goal, Double bmi, AppUserRole appUserRole,
                   AppUserBmiCategories bmiCategories) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.appUserGender = appUserGender;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.bmi = bmi;
        this.appUserRole = appUserRole;
        this.bmiCategories = bmiCategories;
    }

    //Constructor needed for registration
    public AppUser(String name, String email, String password, AppUserRole appUserRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }
}