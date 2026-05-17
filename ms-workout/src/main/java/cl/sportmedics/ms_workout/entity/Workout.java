package cl.sportmedics.ms_workout.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String objective;

    @Column(nullable = false)
    private Integer series;

    @Column(nullable = false)
    private Integer repetitions;

    @Column(nullable = false)
    private String level;
}
