package com.shapesynergy.dietworkout.appuser.service;

import com.shapesynergy.dietworkout.appuser.AppUser;
import com.shapesynergy.dietworkout.appuser.AppUserDTO;
import com.shapesynergy.dietworkout.appuser.AppUserRepository;
import com.shapesynergy.dietworkout.appuser.AppUserRole;
import com.shapesynergy.dietworkout.WorkoutPlans.WorkoutPlans;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@AllArgsConstructor
public class AppUserService {

    @Autowired
    private final AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private final static String USER_NOT_FOUND = "email %s not found";
    public AppUser save(AppUserDTO appUserDTO) {
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

}
