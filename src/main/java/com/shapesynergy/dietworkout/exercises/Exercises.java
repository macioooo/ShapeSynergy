    package com.shapesynergy.dietworkout.exercises;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    @Entity
    @Table (name="exercises")
    @Getter
    @Setter
    public class Exercises {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id_exercise;
        private String exercise;
        Exercises(String exercise) {
            this.exercise = exercise;
        }

        public Exercises() {

        }
    }

