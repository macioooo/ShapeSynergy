package com.shapesynergy.dietworkout.exercises;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlansRepository extends JpaRepository<WorkoutPlans, Integer> {
}
