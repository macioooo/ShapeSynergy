package com.shapesynergy.dietworkout.workoutPlans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkoutPlansRepository extends JpaRepository<WorkoutPlans, Long> {
}
