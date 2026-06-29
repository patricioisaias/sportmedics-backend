# Sistema de Gestión para Gimnasio Sportmedics 🏋️‍♂️

## Descripción del Proyecto
Sistema de gestión para un gimnasio de barrio orientado a optimizar procesos como la gestión de usuarios y membresías, control de asistencia, asignación de rutinas, validación de suscripciones y control de inventario. El sistema está diseñado bajo una arquitectura de microservicios, permitiendo escalabilidad, desacoplamiento y facilidad de mantenimiento.

* **Equipo:** Daniel Aedo, Patricio Céspedes, Angelo Ponce
* **Institución:** Duoc UC
* **Asignatura:** Desarrollo Fullstack I
* **Docente:** Mauricio González V.

---

## Arquitectura y Tecnologías
El sistema está construido sobre un ecosistema distribuido aplicando el patrón **CSR (Controller-Service-Repository)**.

* **Core:** Java 21, Spring Boot 3.x
* **Bases de Datos:** MySQL / Spring Data JPA (Hibernate)
* **Service Discovery & Routing:** Netflix Eureka Server, Spring Cloud Gateway
* **Comunicación Sincrónica:** OpenFeign / WebClient
* **Seguridad:** Spring Security, BCrypt, JJWT (JSON Web Tokens)
* **Documentación API:** SpringDoc OpenAPI (Swagger)

---

## Lista de Microservicios

| Microservicio | Puerto | Base de Datos | Descripción |
| :--- | :--- | :--- | :--- |
| **`service-registry`** | `8761` | *(No aplica)* | Directorio Eureka para que los servicios se encuentren y registren. |
| **`api-gateway`** | `8080` | *(No aplica)* | Enrutador principal y punto de entrada único hacia el ecosistema. |
| **`ms-subscription`** | `8081` | `gym_subscription_db` | Catálogo de planes, precios y control de vigencia. |
| **`ms-member`** | `8082` | `gym_member_db` | Perfiles de socios, métricas corporales y estado activo/inactivo. |
| **`ms-employee`** | `8083` | `gym_employee_db` | Perfiles del staff: profesores, recepcionistas, administradores. |
| **`ms-inventory`** | `8084` | `gym_inventory_db` | Control de activos físicos del gimnasio (máquinas, pesas, suplementos). |
| **`ms-workout`** | `8085` | `gym_workout_db` | Creación de rutinas de hipertrofia, ejercicios y series. |
| **`ms-scheduling`** | `8086` | `gym_scheduling_db` | Agendamiento de clases grupales y evaluaciones con profesores. |
| **`ms-billing`** | `8087` | `gym_billing_db` | Registro de boletas, pagos y control de morosidad. |
| **`ms-access`** | `8088` | `gym_access_db` | Control de ingresos al local validando el estado financiero de la membresía. |
| **`ms-notification`** | `8089` | `gym_notification_db` | Servicio transversal para envío de alertas y comprobantes. |
| **`ms-auth`** | `8090` | `gym_auth_db` | Gestión de credenciales, login, roles y generación de tokens de acceso. |

---

## Guía de Instalación y Ejecución

Para evitar errores de "Service Unavailable" o fallos de conexión entre instancias, el ecosistema debe inicializarse siguiendo estrictamente este orden:

### 1. Preparación de Base de Datos
* Asegurarse de que el motor MySQL (vía XAMPP/Laragon) esté en ejecución en el puerto por defecto **3306**.
* Las **10 bases de datos** listadas en la tabla superior **se crearán automáticamente** al levantar cada microservicio gracias a la configuración de Hibernate en los archivos `application.yml`.
* También puedes utilizar el script `docs/bd-general.sql` si prefieres iniciarlas manualmente.

### 2. Secuencia de Arranque
1. **Ejecutar `service-registry`:** Iniciar este servicio primero. Verificar que el dashboard cargue correctamente en `http://localhost:8761`.
2. **Ejecutar Microservicios de Negocio:** Levantar de forma independiente `ms-auth`, `ms-member`, `ms-subscription`, `ms-inventory`, etc. Refrescar el dashboard de Eureka y asegurarse de que todos aparezcan registrados en estado **UP**.
3. **Ejecutar `api-gateway`:** Este servicio debe ser el **último** en levantarse. Será el único punto de entrada disponible, operando por defecto en el puerto `8080`.

VIDEO TEST
https://drive.google.com/file/d/1i2IO8d3pNlpLtrP9a5FCGTUi7sGSiT-k/view?usp=sharing
