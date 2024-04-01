package com.shapesynergy.dietworkout.appuser.service;

import com.shapesynergy.dietworkout.WorkoutPlans.WorkoutPlans;
import com.shapesynergy.dietworkout.appuser.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        calculateUserBMI(appUser);
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
        return appUserDTO;
    }
    private void calculateUserBMI(AppUser appUser) {
        double height = appUser.getHeight()/100;
        double weight = appUser.getWeight();
        double bmi = (double) Math.round((weight / (height * height))*100)/100;
        if (bmi < 18.5) {
            appUser.setBmiCategories(AppUserBmiCategories.UNDERWEIGHT);
        } else if (bmi >= 18.5 && bmi < 24.9) {
            appUser.setBmiCategories(AppUserBmiCategories.NORMAL);
        } else if (bmi >= 25 && bmi < 29.9) {
            appUser.setBmiCategories(AppUserBmiCategories.OVERWEIGHT);
        } else if (bmi >= 30) {
            appUser.setBmiCategories(AppUserBmiCategories.OBESE);
        }
        appUser.setBmi(bmi);
    }

}
