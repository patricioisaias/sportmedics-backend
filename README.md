# Sportmedics - Sistema de Microservicios 🏋️‍♂️

Sistema de gestión para un gimnasio de barrio orientado a optimizar procesos como la gestión de usuarios y membresías, control de asistencia, asignación de rutinas, validación de suscripciones y control de inventario. El sistema está diseñado bajo una arquitectura de microservicios, permitiendo escalabilidad, desacoplamiento y facilidad de mantenimiento.

* **Equipo:** Daniel Aedo, Patricio Céspedes, Angelo Ponce
* **Institución:** Duoc UC
* **Asignatura:** Desarrollo Fullstack I
* **Docente:** Mauricio González V.

---

# 1. Objetivo del proyecto

El sistema permite gestionar el flujo completo de atención y administración de un gimnasio:

1. Registrar miembros y staff (recepcionistas, entrenadores, admins).
2. Gestionar planes de suscripción y facturación/pagos.
3. Controlar el acceso al recinto validando el estado financiero del socio.
4. Administrar inventario de máquinas y equipamiento.
5. Crear y asignar rutinas de entrenamiento personalizadas.
6. Agendar citas o clases particulares.
7. Enviar notificaciones.

---

# 2. Arquitectura general

```text
Cliente externo / Postman / Navegador
        |
        v
API Gateway :8080
        |
        +--> ms-auth           :8090  -> gym_auth_db
        +--> ms-subscription   :8081  -> gym_subscription_db
        +--> ms-member         :8082  -> gym_member_db
        +--> ms-employee       :8083  -> gym_employee_db
        +--> ms-inventory      :8084  -> gym_inventory_db
        +--> ms-workout        :8085  -> gym_workout_db
        +--> ms-scheduling     :8086  -> gym_scheduling_db
        +--> ms-billing        :8087  -> gym_billing_db
        +--> ms-access         :8088  -> gym_access_db
        +--> ms-notification   :8089  -> gym_notification_db

Eureka Server :8761
```

---

# 3. Microservicios del sistema

| Módulo | Puerto | Responsabilidad |
|---|---|---|
| `service-registry` | 8761 | Directorio Eureka para que los servicios se encuentren y registren. |
| `api-gateway` | 8080 | Enrutador principal y punto de entrada único. |
| `ms-auth` | 8090 | Gestión de credenciales, login, roles y generación de tokens de acceso. |
| `ms-subscription` | 8081 | Catálogo de planes, precios y control de vigencia. |
| `ms-member` | 8082 | Perfiles de socios, métricas corporales y estado activo/inactivo. |
| `ms-employee` | 8083 | Perfiles del staff: profesores, recepcionistas, administradores. |
| `ms-inventory` | 8084 | Control de activos físicos del gimnasio (máquinas, pesas, suplementos). |
| `ms-workout` | 8085 | Creación de rutinas de hipertrofia, ejercicios y series. |
| `ms-scheduling` | 8086 | Agendamiento de clases grupales y evaluaciones con profesores. |
| `ms-billing` | 8087 | Registro de boletas, pagos y control de morosidad. |
| `ms-access` | 8088 | Control de ingresos al local validando el estado de la membresía. |
| `ms-notification` | 8089 | Servicio transversal para envío de alertas y comprobantes. |

---

# 4. Tecnologías utilizadas

El sistema está construido sobre un ecosistema distribuido aplicando el patrón **CSR (Controller-Service-Repository)**.

* Java 21
* Spring Boot 3.x
* Spring Cloud (Netflix Eureka Server & Client)
* Spring Cloud Gateway
* OpenFeign
* Spring Security, BCrypt, JJWT (JSON Web Tokens)
* Spring Data JPA (Hibernate)
* MySQL / XAMPP
* Lombok
* Bean Validation
* SpringDoc OpenAPI (Swagger)
* Maven (Estructura Multimódulo)
* VSCode / IntelliJ

---

# 5. Estructura del proyecto

```text
sportmedics-backend/
|
├── sportmedics-maven/
│   ├── pom.xml
│   ├── docs/
│   │   ├── bd-general.sql
│   │   ├── endpoints.md
│   │   └── orden-ejecucion.md
│   |
│   ├── service-registry/
│   ├── api-gateway/
│   ├── ms-auth/
│   ├── ms-subscription/
│   ├── ms-member/
│   ├── ms-employee/
│   ├── ms-inventory/
│   ├── ms-workout/
│   ├── ms-scheduling/
│   ├── ms-billing/
│   ├── ms-access/
│   └── ms-notification/
|
└── README.md
```

---

# 6. Bases de datos

El proyecto usa una base de datos independiente por microservicio (Patrón Database-per-Service).

| Microservicio | Base de datos |
|---|---|
| `ms-auth` | `gym_auth_db` |
| `ms-subscription` | `gym_subscription_db` |
| `ms-member` | `gym_member_db` |
| `ms-employee` | `gym_employee_db` |
| `ms-inventory` | `gym_inventory_db` |
| `ms-workout` | `gym_workout_db` |
| `ms-scheduling` | `gym_scheduling_db` |
| `ms-billing` | `gym_billing_db` |
| `ms-access` | `gym_access_db` |
| `ms-notification` | `gym_notification_db` |

El script de creación de bases y datos iniciales (opcional, ya que JPA genera las tablas) se encuentra en:

```text
sportmedics-maven/docs/bd-general.sql
```

---

# 7. Configuración de MySQL

Este proyecto asume la ejecución de MySQL (por ejemplo, vía XAMPP) en el puerto por defecto:

```text
3306
```

Ejemplo de configuración en los microservicios (`application.yml`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym_member_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Santiago
spring.datasource.username=root
spring.datasource.password=
```
Las bases de datos se crearán automáticamente al levantar cada microservicio gracias a `createDatabaseIfNotExist=true` y `spring.jpa.hibernate.ddl-auto=update`.

---

# 8. Orden de ejecución

Antes de levantar los microservicios, asegúrese de iniciar XAMPP/MySQL.

Luego, ejecute los servicios en este estricto orden para evitar errores de conexión o "Service Unavailable":

| Orden | Servicio | Puerto |
|---:|---|---:|
| 1 | `service-registry` | 8761 |
| 2 | `ms-auth` | 8090 |
| 3 | `ms-subscription` | 8081 |
| 4 | `ms-member` | 8082 |
| 5 | `ms-employee` | 8083 |
| 6 | `ms-inventory` | 8084 |
| 7 | `ms-workout` | 8085 |
| 8 | `ms-scheduling` | 8086 |
| 9 | `ms-billing` | 8087 |
| 10 | `ms-notification` | 8089 |
| 11 | `ms-access` | 8088 |
| 12 | `api-gateway` | 8080 |

---

# 9. Ejecución desde VSCode / IntelliJ

Se recomienda usar extensiones como **Spring Boot Dashboard** o simplemente correr la clase principal de cada servicio.

Desde consola:
```bash
cd sportmedics-maven/service-registry
mvn spring-boot:run
```

---

# 10. Compilación del proyecto completo

Al usar un proyecto padre Maven (`sportmedics-maven`), se pueden compilar todos los microservicios de una vez desde la raíz `sportmedics-maven/`:

```bash
mvn clean install
```
O si desea saltar las pruebas:
```bash
mvn clean install -DskipTests
```

---

# 11. Eureka Server

La consola de Eureka (Service Registry) se encuentra en:

```text
http://localhost:8761
```

Cuando los servicios estén levantados, se registrarán automáticamente bajo nombres como `API-GATEWAY`, `MS-MEMBER`, etc.

---

# 12. API Gateway

El API Gateway es el punto de entrada principal para consumir las APIs:

```text
http://localhost:8080
```

Rutas principales:

| Recurso | URL |
|---|---|
| Auth | `http://localhost:8080/api/auth` |
| Members | `http://localhost:8080/api/members` |
| Subscriptions | `http://localhost:8080/api/subscriptions` |
| Billings | `http://localhost:8080/api/billings` |
| Inventory | `http://localhost:8080/api/inventory` |
| Workouts | `http://localhost:8080/api/workouts` |
| Schedules | `http://localhost:8080/api/schedules` |
| Accesses | `http://localhost:8080/api/accesses` |

---

# 13. Swagger / OpenAPI

La documentación Swagger se revisa de forma directa por puerto en cada microservicio usando `springdoc-openapi`.
El enlace base es: `http://localhost:[PUERTO]/doc/swagger-ui.html`

Ejemplos:
* **ms-auth:** `http://localhost:8090/doc/swagger-ui.html`
* **ms-member:** `http://localhost:8082/doc/swagger-ui.html`
* **ms-billing:** `http://localhost:8087/doc/swagger-ui.html`
* **ms-inventory:** `http://localhost:8084/doc/swagger-ui.html`

---

# 14. Comunicación entre microservicios

El proyecto usa **OpenFeign** para comunicación sincrónica entre servicios y validación de reglas de negocio.

| Servicio Origen | Servicio Destino | Motivo |
|---|---|---|
| `ms-access` | `ms-billing` | Verificar que el usuario no tenga morosidad para permitir acceso. |
| `ms-access` | `ms-member` | Validar que el miembro existe y obtener sus datos básicos al ingresar. |

---

# 15. Flujo funcional principal

Ejemplo básico de flujo de usuario:
1. **ms-auth:** Registrar credenciales y hacer login para obtener un token JWT.
2. **ms-member:** Registrar los datos personales del nuevo socio.
3. **ms-subscription:** Crear los planes disponibles (Ej. Plan Black).
4. **ms-billing:** Asociar la suscripción al miembro, generando la primera boleta o mes a pagar.
5. **ms-access:** El usuario pasa su ID por el torniquete; el sistema verifica por FeignClient hacia ms-billing si está al día y autoriza el ingreso.

*(Para un detalle completo de endpoints HTTP, revisar `sportmedics-maven/docs/endpoints.md`)*

---

# 16. Validaciones implementadas

Se utiliza `@Valid` y validaciones propias para asegurar la integridad:
* **ms-member:** Validación de formato de correo (`@Email`) y formato de RUT chileno (`@Pattern`).
* **ms-inventory:** Validaciones de cantidad positiva.
* **ms-billing:** Validaciones de monto mayor a cero.

---

# 17. Manejo de errores

Los errores se controlan de manera centralizada en cada microservicio mediante `@RestControllerAdvice` y una clase `GlobalExceptionHandler`. 
Esto asegura que la API responda con códigos HTTP consistentes (400 Bad Request, 404 Not Found, 500 Internal Error) y JSON estructurados.

---

# 18. Logs

Los microservicios usan `@Slf4j` (Lombok) para el registro de trazabilidad.
Ejemplo de consola:
```text
[ms-member] INFO c.s.m.controller.MemberController : Petición POST recibida en /api/members
```

---

# 19. Documentación adicional

La documentación complementaria se encuentra en:
```text
sportmedics-maven/docs/endpoints.md
sportmedics-maven/docs/orden-ejecucion.md
sportmedics-maven/docs/bd-general.sql
```

---

# 20. Estado actual del proyecto

| Elemento | Estado |
|---|---|
| Proyecto Padre Maven | ✅ Implementado |
| Bases de Datos MySQL (Per-Service) | ✅ Implementadas |
| Eureka Server & API Gateway | ✅ Implementados |
| Microservicios de Negocio (10 servicios) | ✅ Implementados |
| Swagger / OpenAPI | ✅ Implementado |
| Feign Client (Sincrónico) | ✅ Implementado |
| Unit Tests (JUnit, Mockito, MockMvc) | ✅ Parcialmente Implementado |
| Seguridad y Roles (Spring Security, JWT) | ✅ Parcialmente Implementado |
| Frontend | ❌ Pendiente |
