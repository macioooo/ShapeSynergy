package com.shapesynergy.dietworkout.appuser.service;

import com.shapesynergy.dietworkout.WorkoutPlans.WorkoutPlans;
import com.shapesynergy.dietworkout.appuser.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class AppUserService {

    @Autowired
    private final AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AppUser save(AppUserDTO appUserDTO) {
        if (checkIfUserExists(appUserDTO.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        AppUser appUser = new AppUser(
                appUserDTO.getName(),
                appUserDTO.getEmail(),
                passwordEncoder.encode(appUserDTO.getPassword()),
                AppUserRole.USER
        );
        return appUserRepository.save(appUser);
    }

    public List<WorkoutPlans> getAllUserWorkoutPlans(Long id_user) {
        List<WorkoutPlans> workoutPlans = appUserRepository.findById(id_user).get().getWorkoutPlans();
        return workoutPlans;
    }

    public boolean checkIfUserExists(String email) {
        return appUserRepository.findByEmail(email) != null;
    }

    public String updateUserInfo(AppUserDTO appUserDTO, Long id_user) {
        AppUser appUser = appUserRepository.findById(id_user).get();
        appUser.setAge(appUserDTO.getAge());
        appUser.setAppUserGender(appUserDTO.getAppUserGender());
        appUser.setActivityLevel(appUserDTO.getActivityLevel());
        appUser.setGoal(appUserDTO.getGoal());
        appUser.setHeight(appUserDTO.getHeight());
        appUser.setWeight(appUserDTO.getWeight());
        appUser.setBmi(calculateUserBMI(appUserDTO));
        appUser.setBmiCategories(calculateUserBmiCategory(appUser.getBmi()));
        appUser.setUserCalories(calculateUserCalories(appUser));
        ArrayList<Integer> macro = calculateUserMacros(appUser.getUserCalories());
        appUser.setUserProtein(macro.get(0));
        appUser.setUserCarbs(macro.get(1));
        appUser.setUserFat(macro.get(2));
        appUserRepository.save(appUser);
        return "User info updated successfully";
    }

    public AppUserDTO getUserInfo(Long id_user) {
        AppUser appUser = appUserRepository.findById(id_user).get();
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setAge(appUser.getAge());
        appUserDTO.setAppUserGender(appUser.getAppUserGender());
        appUserDTO.setActivityLevel(appUser.getActivityLevel());
        appUserDTO.setGoal(appUser.getGoal());
        appUserDTO.setHeight(appUser.getHeight());
        appUserDTO.setWeight(appUser.getWeight());
        appUserDTO.setBmi(appUser.getBmi());
        appUserDTO.setBmiCategories(appUser.getBmiCategories());
        appUserDTO.setUserCalories(appUser.getUserCalories());
        appUserDTO.setUserProtein(appUser.getUserProtein());
        appUserDTO.setUserCarbs(appUser.getUserCarbs());
        appUserDTO.setUserFat(appUser.getUserFat());
        return appUserDTO;
    }

    private double calculateUserBMI(AppUserDTO appUser) {
        double height = appUser.getHeight() / 100;
        double weight = appUser.getWeight();
        double bmi = (double) Math.round((weight / (height * height)) * 100) / 100;
        return bmi;
    }

    private AppUserBmiCategories calculateUserBmiCategory(double bmi) {
        if (bmi < 18.5) {
            return AppUserBmiCategories.UNDERWEIGHT;
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return AppUserBmiCategories.NORMAL;
        } else if (bmi >= 25 && bmi < 29.9) {
            return AppUserBmiCategories.OVERWEIGHT;
        }
        return AppUserBmiCategories.OBESE;
    }


    private int calculateUserCalories(AppUser appUser) {
        double height = appUser.getHeight();
        double weight = appUser.getWeight();
        int age = appUser.getAge();
        AppUserGender gender = appUser.getAppUserGender();
        AppUserActivityLevel activityLevel = appUser.getActivityLevel();
        AppUserGoal goal = appUser.getGoal();
        int bmr = (int) ((10 * weight) + (6.25 * height) - (5 * age));
        if (gender.equals(AppUserGender.MALE)) {
            bmr += 5;
        } else {
            bmr -= 161;
        }
        double activityValue = activityLevel.getActivityLevel();
        double goalValue = goal.getGoal();
        int calories = (int) ((int) (bmr * activityValue) + goalValue);

        return calories;
    }

    private ArrayList<Integer> calculateUserMacros(int calories) {
        ArrayList<Integer> macro = new ArrayList<>();
        int protein = (int) ((calories * 0.25) / 4);
        int carbs = (int) ((calories * 0.5) / 4); // 1 gram of carbs has 4 kcals and should be around 50% of daily calories
        int fat = (int) ((calories * 0.25) / 9);
        macro.add(protein);
        macro.add(carbs);
        macro.add(fat);
        return macro;
    }

}
