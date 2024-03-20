    package com.shapesynergy.dietworkout.exercises;

    import com.shapesynergy.dietworkout.appuser.AppUser;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    @Entity
    @Table (name="workout_plans")
    @Getter
    @Setter
    public class WorkoutPlans {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id_workout_plan;
        private String workout_plan;
        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "id_user", referencedColumnName = "id_user")
        private AppUser user;


        WorkoutPlans(String workout_plan) {
            this.workout_plan = workout_plan;
        }

        public WorkoutPlans() {

        }
    }

