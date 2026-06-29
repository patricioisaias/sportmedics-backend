package cl.sportmedics.ms_auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // Usualmente el email

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role; // ADMIN, TEACHER, USER

    @Column(nullable = false)
    private Boolean active;
}