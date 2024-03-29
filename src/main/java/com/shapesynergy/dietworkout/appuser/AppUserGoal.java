package com.shapesynergy.dietworkout.appuser;

public enum AppUserGoal {
    LOSE_WEIGHT(-500),
    MAINTAIN_WEIGHT(0),
    GAIN_WEIGHT(500);

    private final int goal;

    AppUserGoal(int goal) {
        this.goal = goal;
    }

    public int getGoal() {
        return goal;
    }
}
