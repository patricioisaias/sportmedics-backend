package cl.sportmedics.ms_auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table (name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El username no puede estar vacío")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    @Email(message = "Formato de email inválido")
    private String email;

    private String role; // Ejemplo: ROLE_USER, ROLE_ADMIN
}

