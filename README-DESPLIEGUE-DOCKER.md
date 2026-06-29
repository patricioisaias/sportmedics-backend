# README-DESPLIEGUE-DOCKER.md

# 🐳 Puesta en marcha con Docker — SportMedics

## EP3 - Evaluación Parcial 3 (Microservicios)

---

## 1. Descripción general

Este documento explica cómo ejecutar el ecosistema de microservicios **SportMedics** utilizando **Docker** y **Docker Compose**.

La puesta en marcha con Docker permite levantar todos los componentes necesarios del sistema en contenedores independientes, evitando instalar manualmente cada servicio o configurar cada entorno por separado.

El sistema considera los siguientes componentes:
*   MySQL (Centralizado con un volumen de datos)
*   Eureka Server (Registro)
*   API Gateway (Punto de entrada)
*   10 Microservicios de Negocio (Access, Auth, Billing, Employee, Inventory, Member, Notification, Scheduling, Subscription, Workout).

---

## 2. Objetivo del despliegue

El objetivo de esta puesta en marcha es ejecutar SportMedics como un sistema distribuido. Docker permite que cada componente se ejecute en su propio contenedor y se comuniquen mediante una red interna. 

Con Docker Compose es posible levantar todo el ecosistema mediante un solo comando automatizado en el script `arrancar-sistema.bat`.

---

## 3. Importante: el ZIP no es una imagen Docker completa

El archivo ZIP entregado no corresponde a una imagen Docker. El ZIP corresponde a un **paquete de despliegue** o "entregable" del sistema.

Este paquete incluye:
*   Archivos `.jar` de los microservicios y servidores.
*   Archivo `docker-compose.yml`.
*   Archivo `.env`.
*   Archivo `init.sql`.
*   Scripts `.bat` para levantar, detener, revisar logs, respaldar y restaurar.

Docker utiliza estos archivos para generar las imágenes (usando eclipse-temurin) e inyectar el código para ejecutar los contenedores.

---

## 4. Requisitos previos

Antes de ejecutar el sistema, es obligatorio abrir **Docker Desktop**. Si Docker Desktop no está iniciado, los comandos no funcionarán.

Se debe contar con:
*   Docker Desktop instalado y funcionando.
*   Carpeta de despliegue `sportmedics-docker` descomprimida.
*   Puertos 3307, 8761, 8080 y 8081-8090 disponibles en la máquina host.

---

## 5. Levantar el sistema completo

Para levantar todo el ecosistema:

1. Ubicarse en la carpeta descomprimida `sportmedics-docker`.
2. Ejecutar (haciendo doble clic):
   `arrancar-sistema.bat`

Esto ejecuta internamente `docker compose up -d`. La bandera `-d` levanta los contenedores en segundo plano.

---

## 6. Orden lógico de arranque

El archivo `docker-compose.yml` está configurado con `depends_on` y `healthcheck` para asegurar un inicio seguro:
1. **MySQL**: Inicia primero. Los microservicios no arrancan hasta que MySQL esté "healthy" (saludable).
2. **Eureka Server**: Inicia en paralelo, listo para recibir registros.
3. **Microservicios**: Inician únicamente cuando la base de datos está lista para recibir conexiones.
4. **API Gateway**: Inicia y se registra en Eureka para descubrir las rutas dinámicamente.

---

## 7. Scripts adicionales incluidos

Para facilitar la revisión docente, se incluyen scripts utilitarios:

*   **`detener-sistema.bat`**: Ejecuta `docker compose down`. Detiene la ejecución sin borrar los datos guardados en la base de datos.
*   **`ver-logs.bat`**: Ejecuta `docker compose logs -f`. Permite observar la terminal en tiempo real para hacer depuración.
*   **`backup-db.bat`**: Realiza un volcado (dump) automático de MySQL y lo guarda en la carpeta `backups/`.
*   **`restaurar-db.bat`**: Permite elegir un archivo SQL de la carpeta `backups/` e inyectarlo al contenedor activo.

---

## 8. Persistencia de datos

El sistema utiliza un volumen manejado por Docker (`mysql_data`). Si los contenedores se detienen con `docker compose down`, la información de los pacientes, citas y suscripciones no se pierde.

*Advertencia: Si se ejecuta el comando manual `docker compose down -v`, el volumen se destruirá.*
