# Sportmedics - Endpoints principales

Este documento resume los endpoints principales del sistema **Sportmedics**, un proyecto basado en microservicios con Spring Boot, Eureka, API Gateway, Feign Client, MySQL y Swagger.

## Puertos del sistema

| Servicio | Puerto | Descripción |
|---|---|---|
| Eureka Server | 8761 | Servidor de descubrimiento |
| API Gateway | 8080 | Punto único de entrada |
| ms-subscription | 8081 | Gestión de suscripciones y planes |
| ms-member | 8082 | Gestión de socios o miembros del gimnasio |
| ms-employee | 8083 | Gestión de personal y entrenadores |
| ms-inventory | 8084 | Gestión de inventario de equipos |
| ms-workout | 8085 | Gestión de rutinas de entrenamiento |
| ms-scheduling | 8086 | Gestión de citas y reservas |
| ms-billing | 8087 | Gestión de pagos y facturas |
| ms-access | 8088 | Gestión de accesos y torniquetes |
| ms-notification | 8089 | Envío de notificaciones y alertas |
| ms-auth | 8090 | Autenticación y autorización |

---

# 1. Acceso por API Gateway

El API Gateway permite consumir todos los microservicios desde un único puerto:

```text
http://localhost:8080
```

| Recurso | URL por Gateway |
|---|---|
| Subscription | http://localhost:8080/api/subscriptions |
| Member | http://localhost:8080/api/members |
| Employee | http://localhost:8080/api/employees |
| Inventory | http://localhost:8080/api/inventory |
| Workout | http://localhost:8080/api/workouts |
| Scheduling | http://localhost:8080/api/schedules |
| Billing | http://localhost:8080/api/billings |
| Access | http://localhost:8080/api/accesses |
| Notification | http://localhost:8080/api/notifications |
| Auth | http://localhost:8080/api/auth |

---

# 2. Swagger por microservicio

Swagger se revisa directamente desde cada microservicio usando springdoc-openapi:

| Microservicio | Swagger |
|---|---|
| ms-subscription | http://localhost:8081/doc/swagger-ui.html |
| ms-member | http://localhost:8082/doc/swagger-ui.html |
| ms-employee | http://localhost:8083/doc/swagger-ui.html |
| ms-inventory | http://localhost:8084/doc/swagger-ui.html |
| ms-workout | http://localhost:8085/doc/swagger-ui.html |
| ms-scheduling | http://localhost:8086/doc/swagger-ui.html |
| ms-billing | http://localhost:8087/doc/swagger-ui.html |
| ms-access | http://localhost:8088/doc/swagger-ui.html |
| ms-notification | http://localhost:8089/doc/swagger-ui.html |
| ms-auth | http://localhost:8090/doc/swagger-ui.html |

---

# 3. Microservicio Subscription

## Puerto directo

```text
http://localhost:8081
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/subscriptions | Crear una suscripción |
| GET | /api/subscriptions | Obtener todos los suscripciones |
| GET | /api/subscriptions/{id} | Obtener una suscripción por ID |
| PUT | /api/subscriptions/{id} | Actualizar una suscripción |
| DELETE | /api/subscriptions/{id} | Eliminar una suscripción |

---

# 4. Microservicio Member

## Puerto directo

```text
http://localhost:8082
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/members | Crear un miembro |
| GET | /api/members | Obtener todos los miembros |
| GET | /api/members/{id} | Obtener un miembro por ID |
| PUT | /api/members/{id} | Actualizar un miembro |
| DELETE | /api/members/{id} | Eliminar un miembro |

---

# 5. Microservicio Employee

## Puerto directo

```text
http://localhost:8083
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/employees | Crear un empleado |
| GET | /api/employees | Obtener todos los empleados |
| GET | /api/employees/{id} | Obtener un empleado por ID |
| PUT | /api/employees/{id} | Actualizar un empleado |
| DELETE | /api/employees/{id} | Eliminar un empleado |

---

# 6. Microservicio Inventory

## Puerto directo

```text
http://localhost:8084
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/inventory | Crear un artículo del inventario |
| GET | /api/inventory | Obtener todos los artículos |
| GET | /api/inventory/{id} | Obtener un artículo del inventario por ID |
| GET | /api/inventory/category/{category} | Obtener un artículo del inventario por ID |
| PUT | /api/inventory/{id} | Actualizar un artículo del inventario |
| DELETE | /api/inventory/{id} | Eliminar un artículo del inventario |

---

# 7. Microservicio Workout

## Puerto directo

```text
http://localhost:8085
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/workouts | Crear una rutina |
| GET | /api/workouts | Obtener todos los rutinas |
| GET | /api/workouts/{id} | Obtener una rutina por ID |
| DELETE | /api/workouts/{id} | Eliminar una rutina |

---

# 8. Microservicio Scheduling

## Puerto directo

```text
http://localhost:8086
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/schedules | Crear una cita |
| GET | /api/schedules | Obtener todos los citas |
| GET | /api/schedules/{id} | Obtener una cita por ID |
| GET | /api/schedules/member/{memberId} | Obtener citas por miembro |
| PATCH | /api/schedules/{id}/status | Modificar parcialmente una cita |
| DELETE | /api/schedules/{id} | Eliminar una cita |

---

# 9. Microservicio Billing

## Puerto directo

```text
http://localhost:8087
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/billings | Crear una factura |
| GET | /api/billings | Obtener todos los facturas |
| GET | /api/billings/{id} | Obtener una factura por ID |
| PUT | /api/billings/{id} | Actualizar una factura |
| DELETE | /api/billings/{id} | Eliminar una factura |
| POST | /api/billings/{id}/payments | Crear una factura |
| GET | /api/billings/status/{memberId} | Obtener facturas por miembro |
| POST | /api/billings/initialize/{memberId} | Crear una factura |

---

# 10. Microservicio Access

## Puerto directo

```text
http://localhost:8088
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/accesses/verify | Verificar e ingresar |
| GET | /api/accesses | Obtener todos los registros de acceso |
| GET | /api/accesses/member/{memberId} | Obtener registros de acceso por miembro |

---

# 11. Microservicio Notification

## Puerto directo

```text
http://localhost:8089
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/notifications/send | Crear una notificación |
| GET | /api/notifications | Obtener todos los notificaciones |
| GET | /api/notifications/email | Obtener todos los notificaciones |

---

# 12. Microservicio Auth

## Puerto directo

```text
http://localhost:8090
```

## Por API Gateway

```text
http://localhost:8080
```

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/auth/register | Registrarse |
| POST | /api/auth/login | Iniciar sesión |
| GET | /api/auth/users | Obtener todos los sesiones |
| DELETE | /api/auth/users/{id} | Eliminar una sesión |

---

# Resumen de comunicaciones Feign

El sistema utiliza Feign Clients para comunicarse internamente.

| Servicio origen | Servicio destino | Motivo |
|---|---|---|
| ms-access | ms-billing | Comunicación interna requerida |
| ms-access | ms-member | Comunicación interna requerida |

---

# Recomendación de uso en clases

Para revisar documentación técnica de cada API, usar Swagger directo:

```text

http://localhost:8081/doc/swagger-ui.html
http://localhost:8082/doc/swagger-ui.html
http://localhost:8083/doc/swagger-ui.html
http://localhost:8084/doc/swagger-ui.html
http://localhost:8085/doc/swagger-ui.html
http://localhost:8086/doc/swagger-ui.html
http://localhost:8087/doc/swagger-ui.html
http://localhost:8088/doc/swagger-ui.html
http://localhost:8089/doc/swagger-ui.html
http://localhost:8090/doc/swagger-ui.html
```

Para probar el flujo completo del sistema, usar API Gateway:

```text
http://localhost:8080
```

De esta forma se demuestra:

* documentación individual por microservicio;

* consumo centralizado por API Gateway;

* registro de servicios en Eureka;

* comunicación real mediante Feign Client (si aplica);

* separación de bases de datos.
