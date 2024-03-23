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

    private final static String USER_NOT_FOUND = "email %s not found";
    @Autowired
    private final AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

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

    public String updateUserInfo(AppUserDTO appUserDTO, Long id_user) {
        AppUser appUser = appUserRepository.findById(id_user).get();
        appUser.setAge(appUserDTO.getAge());
        appUser.setAppUserGender(appUserDTO.getAppUserGender());
        appUser.setActivityLevel(appUserDTO.getActivityLevel());
        appUser.setGoal(appUserDTO.getGoal());
        appUser.setHeight(appUserDTO.getHeight());
        appUser.setWeight(appUserDTO.getWeight());
        appUser.setBmi(appUserDTO.getBmi());
        appUserRepository.save(appUser);
        return "User info updated successfully";
    }
}
