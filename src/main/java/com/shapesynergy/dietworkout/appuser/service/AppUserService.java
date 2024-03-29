package com.shapesynergy.dietworkout.appuser.service;

import com.shapesynergy.dietworkout.WorkoutPlans.WorkoutPlans;
import com.shapesynergy.dietworkout.appuser.AppUser;
import com.shapesynergy.dietworkout.appuser.AppUserDTO;
import com.shapesynergy.dietworkout.appuser.AppUserRepository;
import com.shapesynergy.dietworkout.appuser.AppUserRole;
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
    private void calculateUserBMI(AppUser appUser) {
        double height = appUser.getHeight()/100;
        double weight = appUser.getWeight();
        double bmi = (double) Math.round((weight / (height * height))*100)/100;
        appUser.setBmi(bmi);
    }

}
