package cl.sportmedics.ms_workout.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @Column(name = "muscle_group", nullable = false)
    private String muscleGroup;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;

    @Column(name = "rest_seconds")
    private Integer restSeconds;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;
}