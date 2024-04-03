package com.shapesynergy.dietworkout.appuser;

public enum AppUserActivityLevel {

    SEDENTARY(1.2),
    LIGHTLY_ACTIVE(1.375),
    MODERATELY_ACTIVE(1.55),
    VERY_ACTIVE(1.725),
    SUPER_ACTIVE(1.9);

    private final double activityLevel;

    AppUserActivityLevel(double activityLevel) {
        this.activityLevel = activityLevel;
    }

    public double getActivityLevel() {
        return activityLevel;
    }
}
