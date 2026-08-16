# Sportmedics - Orden de ejecución

## Requisitos previos

Antes de levantar el sistema se debe tener iniciado:

- XAMPP
- MySQL en puerto 3306
- Eureka Server (service-registry)
- Microservicios de negocio
- API Gateway

## Bases de datos

El proyecto usa una base de datos independiente por microservicio:

| Microservicio   | Base datos          |
|-----------------|---------------------|
| ms-access       | gym_access_db       |
| ms-auth         | gym_auth_db         |
| ms-billing      | gym_billing_db      |
| ms-employee     | gym_employee_db     |
| ms-inventory    | gym_inventory_db    |
| ms-member       | gym_member_db       |
| ms-notification | gym_notification_db |
| ms-scheduling   | gym_scheduling_db   |
| ms-subscription | gym_subscription_db |
| ms-workout      | gym_workout_db      |

El script de creación se encuentra en:

```text
docs/bd-general.sql
```
