# Sistema de Gestión para Gimnasio Sportmedics 🏋️‍♂️

## Descripción del Proyecto

Sistema de gestión para un gimnasio de barrio orientado a optimizar procesos como la gestión de usuarios y membresías, control de asistencia, asignación de rutinas y validación de suscripciones. El sistema está diseñado bajo una arquitectura de microservicios, permitiendo escalabilidad, desacoplamiento y facilidad de mantenimiento.

- **Equipo:** Daniel Aedo, Patricio Céspedes, Angelo Ponce
- **Institución:** Duoc UC
- **Asignatura:** Desarrollo Fullstack I
- **Docente:** Mauricio González V

---

## Arquitectura y Tecnologías

El sistema utiliza una arquitectura de microservicios.

- **Core:** Java 21, Spring Boot
- **Bases de Datos:** MySQL (XAMPP) / Spring Data JPA
- **Service Discovery & Routing:** Netflix Eureka Server, Spring Cloud Gateway
- **Comunicación Sincrónica:** OpenFeign
- **Seguridad:** Spring Security, BCrypt, JJWT (JSON Web Tokens)
- **Documentación API:** SpringDoc OpenAPI (Swagger)

---

## Lista de Microservicios

| Microservicio     | Base de Datos         | Descripción                                                             |
| :---------------- | :-------------------- | :---------------------------------------------------------------------- |
| `serviceregistry` | _(No aplica)_         | Directorio Eureka para que los servicios se encuentren.                 |
| `apigateway`      | _(No aplica)_         | Enrutador principal de peticiones hacia el ecosistema.                  |
| `ms-auth`         | `gym_auth_db`         | Gestión de credenciales, login, roles y generación de tokens de acceso. |
| `ms-employee`     | `gym_employee_db`     | Perfiles del staff: profesores, recepcionistas, administradores.        |
| `ms-member`       | `gym_member_db`       | Perfiles de socios, métricas corporales y estado activo/inactivo.       |
| `ms-subscription` | `gym_subscription_db` | Catálogo de planes, precios y control de vigencia.                      |
| `ms-workout`      | `gym_workout_db`      | Creación de rutinas de hipertrofia, ejercicios y series.                |
| `ms-scheduling`   | `gym_scheduling_db`   | Agendamiento de clases grupales y evaluaciones.                         |
| `ms-billing`      | `gym_billing_db`      | Registro de boletas y control de morosidad.                             |
| `ms-access`       | `gym_access_db`       | Control de ingresos al local validando estado de la membresía.          |

---

## Guía de Instalación y Ejecución

Para evitar errores de "Service Unavailable" o fallos de conexión, el ecosistema debe inicializarse siguiendo estrictamente el siguiente orden:

### 1. Preparación de Base de Datos

- Asegurarse de que el motor MySQL (vía XAMPP/Laragon) esté en ejecución.
- Crear manualmente las bases de datos listadas en la tabla superior antes de iniciar los servicios. _(Nota: Los servicios están configurados para conectarse al puerto 3307 por defecto, ajusta tu `application.properties` si usas el 3306)._

### 2. Secuencia de Arranque

1.  **Ejecutar `serviceregistry`:** Iniciar este servicio primero y verificar que el dashboard cargue en `http://localhost:8761`.
2.  **Ejecutar Microservicios de Negocio:** Iniciar `ms-auth`, `ms-member`, etc. Refresca el dashboard de Eureka y asegurarse de que todos aparezcan registrados en estado **UP**.
3.  **Ejecutar `apigateway`:** Este servicio debe ser el último en levantarse. Único punto de entrada disponible en el puerto `8080`.

---

## Documentación y Pruebas

La documentación interactiva de los endpoints está generada con Swagger. Una vez que los servicios estén arriba, se pueden probar las APIs accediendo a la ruta de la interfaz de usuario en el navegador local (por ejemplo: `http://localhost:[PUERTO-DEL-SERVICIO]/swagger-ui.html`).
