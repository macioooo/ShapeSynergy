package com.shapesynergy.dietworkout.scheduler;

import com.shapesynergy.dietworkout.appuser.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ResetCaloriesTask {
    private final AppUserService userService;

    @Scheduled(cron = "0 0 0 * * ?") //every day at 00:00
    public void resetCalories() {
        userService.resetAllUserCalories();

    }
}
