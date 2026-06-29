# SportMedics - Sistema de Microservicios (Desarrollo y Arquitectura)

SportMedics es un sistema para la gestión integral de un centro médico deportivo y gimnasio. Permite administrar accesos, autenticación, facturación, empleados, inventario, miembros, notificaciones, horarios, suscripciones y entrenamientos mediante una arquitectura de microservicios desarrollada con Spring Boot.

Este proyecto está diseñado aplicando patrones de arquitectura de microservicios, Eureka Server, API Gateway, Feign Client, MySQL, Swagger/OpenAPI y estructura Maven padre-hijos.

---

# 1. Objetivo del proyecto

El sistema permite gestionar el flujo completo de atención y operaciones de SportMedics:
1. Registrar miembros y empleados.
2. Controlar los accesos y la autenticación segura.
3. Administrar el inventario y equipos.
4. Programar horarios y rutinas de entrenamiento.
5. Gestionar suscripciones, facturación y notificaciones automáticas.

---

# 2. Arquitectura general

```text
Cliente externo / Postman / Navegador
        |
        v
API Gateway :8080
        |
        +--> ms-member        :8081  -> gym_member_db
        +--> ms-subscription  :8082  -> gym_subscription_db
        +--> ms-workout       :8083  -> gym_workout_db
        +--> ms-inventory     :8084  -> gym_inventory_db
        +--> ms-billing       :8085  -> gym_billing_db
        +--> ms-scheduling    :8086  -> gym_scheduling_db
        +--> ms-employee      :8087  -> gym_employee_db
        +--> ms-access        :8088  -> gym_access_db
        +--> ms-notification  :8089  -> gym_notification_db
        +--> ms-auth          :8090  -> gym_auth_db

Eureka Server :8761
```

---

# 3. Microservicios del sistema

| Módulo | Puerto | Responsabilidad |
| :--- | :--- | :--- |
| `eureka-server` | 8761 | Registro y descubrimiento de servicios |
| `api-gateway` | 8080 | Punto único de entrada a las APIs |
| `ms-member` | 8081 | Administración de miembros/clientes |
| `ms-subscription` | 8082 | Gestión de planes y suscripciones |
| `ms-workout` | 8083 | Rutinas y entrenamientos |
| `ms-inventory` | 8084 | Control de equipos e inventario |
| `ms-billing` | 8085 | Facturación y pagos |
| `ms-scheduling` | 8086 | Horarios y agenda |
| `ms-employee` | 8087 | Gestión del personal |
| `ms-access` | 8088 | Control de acceso físico |
| `ms-notification` | 8089 | Envío de alertas y correos |
| `ms-auth` | 8090 | Autenticación y seguridad |

---

# 4. Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Cloud
* Eureka Server & Client
* Spring Cloud Gateway
* OpenFeign
* Spring Web
* Spring Data JPA
* MySQL
* Lombok
* Bean Validation
* Swagger / OpenAPI
* Maven

---

# 5. Bases de datos

El proyecto usa una base de datos independiente por microservicio para asegurar el desacoplamiento:

| Microservicio | Base de datos |
| :--- | :--- |
| `ms-member` | `gym_member_db` |
| `ms-subscription` | `gym_subscription_db` |
| `ms-workout` | `gym_workout_db` |
| `ms-inventory` | `gym_inventory_db` |
| `ms-billing` | `gym_billing_db` |
| `ms-scheduling` | `gym_scheduling_db` |
| `ms-employee` | `gym_employee_db` |
| `ms-access` | `gym_access_db` |
| `ms-notification` | `gym_notification_db` |
| `ms-auth` | `gym_auth_db` |

El script de creación de bases de datos inicial se encuentra en:
`docs/bd-general.sql`

---

# 6. Orden de ejecución (Fase Nativa)

Antes de levantar los microservicios, se debe iniciar el motor de MySQL (ej. usando XAMPP) en el puerto `3306`.

Luego se deben ejecutar los servicios en el siguiente orden estricto:

| Orden | Servicio | Puerto |
| :--- | :--- | :--- |
| 1 | `eureka-server` | 8761 |
| 2 a 11 | *Todos los microservicios de negocio* | 8081 al 8090 |
| 12 | `api-gateway` | 8080 |

*Nota: Se provee un script `arrancar-nativo.bat` en la versión empaquetada que respeta estos tiempos de inicio.*

---

# 7. Swagger y Documentación de APIs

El proyecto integra OpenAPI 3 (Swagger). Para acceder a la documentación interactiva, dirígete al puerto de cada microservicio respectivo, por ejemplo:
`http://localhost:8081/doc/swagger-ui.html`

El API Gateway centraliza las peticiones hacia el exterior, pero el desarrollo y testeo directo se hace por puerto individual.

---

# 8. Comandos Útiles de Maven

## Compilar todo el proyecto
```bash
mvn clean install -DskipTests
```

## Compilar y ejecutar pruebas unitarias
```bash
mvn clean test
```

## Ejecutar un microservicio (Desde la carpeta del módulo)
```bash
mvn spring-boot:run
```
