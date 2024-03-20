package com.shapesynergy.dietworkout.appuser.service;

import com.shapesynergy.dietworkout.appuser.AppUser;
import com.shapesynergy.dietworkout.appuser.AppUserDTO;
import com.shapesynergy.dietworkout.appuser.AppUserRepository;
import com.shapesynergy.dietworkout.appuser.AppUserRole;
import com.shapesynergy.dietworkout.exercises.WorkoutPlans;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@AllArgsConstructor
public class AppUserService {

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
    public List<String> getUserWorkoutPlan(Long id_user) {
        List<WorkoutPlans> workoutPlans = appUserRepository.findById(id_user).get().getWorkoutPlans();
        String workoutPlan = workoutPlans.get(0).getWorkout_plan();
        List<String> workoutPlanList = new ArrayList<>(Arrays.asList(workoutPlan.split(", ")));
        return workoutPlanList;
    }
}
