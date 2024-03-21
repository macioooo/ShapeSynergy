package com.shapesynergy.dietworkout.appuser;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    @GetMapping("/getUserWorkoutPlans")
    public ResponseEntity<List<String>> getUserWorkoutPlans(@AuthenticationPrincipal CustomAppUserDetails userDetails){
        List<String> workoutPlan = appUserService.getUserWorkoutPlan(userDetails.getId());
        if (workoutPlan.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(workoutPlan);
    }
    @GetMapping("/userWorkoutPlans")
    public String getUserWorkoutPlansPage() {
        return "userWorkoutPlans";
    }
}
