package com.shapesynergy.dietworkout.appuser;

import com.shapesynergy.dietworkout.WorkoutPlans.WorkoutPlans;
import com.shapesynergy.dietworkout.appuser.service.AppUserService;
import com.shapesynergy.dietworkout.appuser.service.CustomAppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class AppUserController {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private final AppUserService appUserService;

    @GetMapping("/")
    public String preventUserFromAccesingHomePage(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails!=null) {
            return "redirect:/user";
        }
    return "index";
    }

    @GetMapping("/exercises")
    public String getExercisesPage() {
        return "exercises";
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
    public String loginError(RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password");
        return "redirect:/login";
    }




    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "user";
    }
    @GetMapping("/admin")
    public String getAdminPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "admin";
    }

//Workout

    @GetMapping("/userWorkoutPlans")
    public String getUserWorkoutPlans(Model model, @AuthenticationPrincipal CustomAppUserDetails userDetails) {
        List<WorkoutPlans> workoutPlans = appUserService.getAllUserWorkoutPlans(userDetails.getId());
        model.addAttribute("workoutPlans", workoutPlans);
        return "userWorkoutPlans"; // Replace "your-template-name" with the actual name of your Thymeleaf template
    }

    //redirect to userWorkoutPlans after deleting a workout plan
    @GetMapping("exercises/deleteWorkoutPlan/**")
        public String deleteWorkoutPlan() {
            return "redirect:/userWorkoutPlans";
        }





}
