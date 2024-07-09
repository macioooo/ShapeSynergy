package com.shapesynergy.dietworkout.appuser;

import com.shapesynergy.dietworkout.workoutPlans.*;
import com.shapesynergy.dietworkout.appuser.service.AppUserService;
import com.shapesynergy.dietworkout.appuser.service.CustomAppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class AppUserController {
    @Autowired
    private final AppUserService appUserService;
    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/")
    public String preventUserFromAccesingHomePage(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return "redirect:/user";
        }
        return "index";
    }

    @GetMapping("user/exercises")
    public String getExercisesPage() {
        return "user/exercises";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            return "redirect:/user";
        }
        return "register";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") AppUserDTO appUserDTO, RedirectAttributes redirectAttributes) {
        if (appUserService.checkIfUserExists(appUserDTO.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email is already taken");
            return "redirect:/registration";
        }
        appUserService.save(appUserDTO);
        redirectAttributes.addFlashAttribute("successMessage", "User registered successfully");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return "redirect:/user";
        }
        return "login";
    }

    @GetMapping("/loginerror")
    public String loginError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password");
        return "redirect:/login";
    }


    //USER PANEL
    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "/user/user";
    }

    @GetMapping("/user/info")
    public String getUserInfoPage(Model model, @AuthenticationPrincipal CustomAppUserDetails userDetails) {
        AppUserDTO appUser = appUserService.getUserInfo(userDetails.getId());
        model.addAttribute("user", appUser);
        return "user/userinfo";
    }

    @PostMapping("/user/info")
    public String updateUserInfo(@ModelAttribute("appUserDTO") AppUserDTO appUserDTO, @AuthenticationPrincipal CustomAppUserDetails userDetails) {

        appUserService.updateUserInfo(appUserDTO, userDetails.getId());
        return "redirect:/user/info";
    }


    @GetMapping("/admin")
    public String getAdminPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "admin";
    }

//Workout

    @GetMapping("/user/workoutplans")
    public String getUserWorkoutPlans(Model model, @AuthenticationPrincipal CustomAppUserDetails userDetails) {
        List<WorkoutPlans> workoutPlans = appUserService.getAllUserWorkoutPlans(userDetails.getId());
        model.addAttribute("workoutPlans", workoutPlans);
        return "user/userWorkoutPlans";
    }

    //redirect to userWorkoutPlans after deleting a workout plan
    @GetMapping("exercises/deleteWorkoutPlan/**")
    public String deleteWorkoutPlan() {
        return "redirect:user/userWorkoutPlans";
    }


}
