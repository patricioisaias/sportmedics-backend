# Sistema de Gestión para Gimnasio Sportmedics 🏋️‍♂️

## 📖 Descripción del Proyecto
Sistema de gestión para un gimnasio de barrio orientado a optimizar procesos como la gestión de usuarios y membresías, control de asistencia, asignación de rutinas, validación de suscripciones y control de inventario. El sistema está diseñado bajo una arquitectura de microservicios, permitiendo escalabilidad, desacoplamiento y facilidad de mantenimiento.

* **Equipo:** Daniel Aedo, Patricio Céspedes, Angelo Ponce
* **Institución:** Duoc UC
* **Asignatura:** Desarrollo Fullstack I
* **Docente:** Mauricio González V.

---

## 🛠️ Arquitectura y Tecnologías
El sistema está construido sobre un ecosistema distribuido aplicando el patrón **CSR (Controller-Service-Repository)**.

* **Core:** Java 21, Spring Boot 3.x
* **Bases de Datos:** MySQL / Spring Data JPA (Hibernate)
* **Service Discovery & Routing:** Netflix Eureka Server, Spring Cloud Gateway
* **Comunicación Sincrónica:** OpenFeign / WebClient
* **Seguridad:** Spring Security, BCrypt, JJWT (JSON Web Tokens)
* **Documentación API:** SpringDoc OpenAPI (Swagger)

---

## 📦 Lista de Microservicios

| Microservicio | Base de Datos | Descripción |
| :--- | :--- | :--- |
| **`service-registry`** | *(No aplica)* | Directorio Eureka para que los servicios se encuentren y registren. |
| **`api-gateway`** | *(No aplica)* | Enrutador principal y punto de entrada único hacia el ecosistema. |
| **`ms-auth`** | `gym_auth_db` | Gestión de credenciales, login, roles y generación de tokens de acceso. |
| **`ms-employee`** | `gym_employee_db` | Perfiles del staff: profesores, recepcionistas, administradores. |
| **`ms-member`** | `gym_member_db` | Perfiles de socios, métricas corporales y estado activo/inactivo. |
| **`ms-subscription`** | `gym_subscription_db` | Catálogo de planes, precios y control de vigencia. |
| **`ms-workout`** | `gym_workout_db` | Creación de rutinas de hipertrofia, ejercicios y series. |
| **`ms-scheduling`** | `gym_scheduling_db` | Agendamiento de clases grupales y evaluaciones con profesores. |
| **`ms-billing`** | `gym_billing_db` | Registro de boletas, pagos y control de morosidad. |
| **`ms-access`** | `gym_access_db` | Control de ingresos al local validando el estado financiero de la membresía. |
| **`ms-inventory`** | `gym_inventory_db` | Control de activos físicos del gimnasio (máquinas, pesas, suplementos). |
| **`ms-notification`** | `gym_notification_db` | Servicio transversal para envío de alertas y comprobantes. |

---

## 🚀 Guía de Instalación y Ejecución

Para evitar errores de "Service Unavailable" o fallos de conexión entre instancias, el ecosistema debe inicializarse siguiendo estrictamente este orden:

### 1. Preparación de Base de Datos
* Asegurarse de que el motor MySQL (vía XAMPP/Laragon) esté en ejecución.
* Crear manualmente las **10 bases de datos** listadas en la tabla superior antes de iniciar los servicios. 
* *(Nota: Los servicios están configurados para conectarse al puerto `3307` por defecto, ajusta tu `application.properties` o `application.yml` al puerto `3306` si utilizas la configuración estándar de XAMPP).*

### 2. Secuencia de Arranque
1. **Ejecutar `service-registry`:** Iniciar este servicio primero. Verificar que el dashboard cargue correctamente en `http://localhost:8761`.
2. **Ejecutar Microservicios de Negocio:** Levantar de forma independiente `ms-auth`, `ms-member`, `ms-subscription`, `ms-inventory`, etc. Refrescar el dashboard de Eureka y asegurarse de que todos aparezcan registrados en estado **UP**.
3. **Ejecutar `api-gateway`:** Este servicio debe ser el **último** en levantarse. Será el único punto de entrada disponible, operando por defecto en el puerto `8080`.

---

## 🧪 Documentación y Pruebas

La documentación interactiva de los endpoints está generada automáticamente con Swagger. Una vez que los servicios estén arriba y registrados, se pueden visualizar y probar las APIs accediendo a la ruta de la interfaz en el navegador local:

👉 `http://localhost:[PUERTO-DEL-SERVICIO]/swagger-ui.html`

*(Asegúrate de reemplazar `[PUERTO-DEL-SERVICIO]` por el puerto específico asignado a cada microservicio en su respectivo archivo properties).*
