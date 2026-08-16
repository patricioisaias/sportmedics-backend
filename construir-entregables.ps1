Write-Host "=========================================================="
Write-Host "       CONSTRUYENDO ENTREGABLES - SPORTMEDICS"
Write-Host "=========================================================="
Write-Host ""

Write-Host "[1/4] Compilando proyecto completo con Maven..."
$process = Start-Process -FilePath "cmd.exe" -ArgumentList "/c .\mvnw.cmd clean install -DskipTests" -Wait -PassThru
if ($process.ExitCode -ne 0) {
    Write-Host "ERROR: La compilacion fallo. Revisa los errores de Maven." -ForegroundColor Red
    exit $process.ExitCode
}

Write-Host ""
Write-Host "[2/4] Creando estructura de directorios..."
$DOCKER_DIR = "sportmedics-docker"
$NATIVO_DIR = "sportmedics-nativo"

if (Test-Path $DOCKER_DIR) { Remove-Item -Recurse -Force $DOCKER_DIR }
if (Test-Path $NATIVO_DIR) { Remove-Item -Recurse -Force $NATIVO_DIR }

New-Item -ItemType Directory -Path "$DOCKER_DIR\apps" | Out-Null
New-Item -ItemType Directory -Path "$DOCKER_DIR\docs" | Out-Null
New-Item -ItemType Directory -Path "$DOCKER_DIR\backups" | Out-Null

New-Item -ItemType Directory -Path "$NATIVO_DIR\apps" | Out-Null

Write-Host ""
Write-Host "[3/4] Copiando archivos binarios (.jar) y scripts..."
$modules = @("api-gateway", "ms-access", "ms-auth", "ms-billing", "ms-employee", "ms-inventory", "ms-member", "ms-notification", "ms-scheduling", "ms-subscription", "ms-workout", "service-registry")

foreach ($mod in $modules) {
    Copy-Item -Path "$mod\target\$mod-0.0.1-SNAPSHOT.jar" -Destination "$DOCKER_DIR\apps\$mod.jar" -Force
    Copy-Item -Path "$mod\target\$mod-0.0.1-SNAPSHOT.jar" -Destination "$NATIVO_DIR\apps\$mod.jar" -Force
}

Copy-Item -Path "docs\bd-general.sql" -Destination "$DOCKER_DIR\docs\init.sql" -Force

Write-Host ""
Write-Host "[4/4] Generando archivos de configuracion para Docker..."

# Generar .env
$envContent = @"
MYSQL_ROOT_PASSWORD=root
MYSQL_USER=sportmedics
MYSQL_PASSWORD=sportmedics123
"@
Set-Content -Path "$DOCKER_DIR\.env" -Value $envContent -Encoding Ascii

# Generar docker-compose.yml
$dockerComposeContent = @"
version: '3.8'
services:
  # Base de Datos
  sportmedics-mysql:
    image: mysql:8.0
    container_name: sportmedics-mysql
    environment:
      MYSQL_ROOT_PASSWORD: `${MYSQL_ROOT_PASSWORD}
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./docs/init.sql:/docker-entrypoint-initdb.d/init.sql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
      interval: 5s
      timeout: 5s
      retries: 10

  # Service Registry (Eureka)
  eureka-server:
    image: eclipse-temurin:21-jre-alpine
    container_name: eureka-server
    ports:
      - "8761:8761"
    volumes:
      - ./apps/service-registry.jar:/app/service-registry.jar
    command: java -jar /app/service-registry.jar

  # API Gateway
  api-gateway:
    image: eclipse-temurin:21-jre-alpine
    container_name: api-gateway
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/api-gateway.jar:/app/api-gateway.jar
    command: java -jar /app/api-gateway.jar
    depends_on:
      eureka-server:
        condition: service_started

  # Microservicio: ms-access
  ms-access:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-access
    ports:
      - "8088:8088"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-access.jar:/app/ms-access.jar
    command: java -jar /app/ms-access.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-auth
  ms-auth:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-auth
    ports:
      - "8090:8090"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-auth.jar:/app/ms-auth.jar
    command: java -jar /app/ms-auth.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-billing
  ms-billing:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-billing
    ports:
      - "8087:8087"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-billing.jar:/app/ms-billing.jar
    command: java -jar /app/ms-billing.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-employee
  ms-employee:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-employee
    ports:
      - "8083:8083"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-employee.jar:/app/ms-employee.jar
    command: java -jar /app/ms-employee.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-inventory
  ms-inventory:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-inventory
    ports:
      - "8084:8084"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-inventory.jar:/app/ms-inventory.jar
    command: java -jar /app/ms-inventory.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-member
  ms-member:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-member
    ports:
      - "8082:8082"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-member.jar:/app/ms-member.jar
    command: java -jar /app/ms-member.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-notification
  ms-notification:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-notification
    ports:
      - "8089:8089"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-notification.jar:/app/ms-notification.jar
    command: java -jar /app/ms-notification.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-scheduling
  ms-scheduling:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-scheduling
    ports:
      - "8086:8086"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-scheduling.jar:/app/ms-scheduling.jar
    command: java -jar /app/ms-scheduling.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-subscription
  ms-subscription:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-subscription
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-subscription.jar:/app/ms-subscription.jar
    command: java -jar /app/ms-subscription.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

  # Microservicio: ms-workout
  ms-workout:
    image: eclipse-temurin:21-jre-alpine
    container_name: ms-workout
    ports:
      - "8085:8085"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./apps/ms-workout.jar:/app/ms-workout.jar
    command: java -jar /app/ms-workout.jar
    depends_on:
      sportmedics-mysql:
        condition: service_healthy
      eureka-server:
        condition: service_started

volumes:
  mysql_data:
"@
Set-Content -Path "$DOCKER_DIR\docker-compose.yml" -Value $dockerComposeContent -Encoding Ascii

# Generar arrancar-sistema.bat
$arrancarSistemaContent = @"
@echo off
echo Levantando contenedores en Docker...
docker compose up -d
echo.
echo Puedes verificar los servicios con: docker compose ps
pause
"@
Set-Content -Path "$DOCKER_DIR\arrancar-sistema.bat" -Value $arrancarSistemaContent -Encoding Ascii

# Generar detener-sistema.bat
$detenerSistemaContent = @"
@echo off
echo Deteniendo contenedores en Docker...
docker compose down
echo.
echo Contenedores detenidos. Los datos siguen seguros en el volumen.
pause
"@
Set-Content -Path "$DOCKER_DIR\detener-sistema.bat" -Value $detenerSistemaContent -Encoding Ascii

# Generar ver-logs.bat
$verLogsContent = @"
@echo off
echo Mostrando logs (Ctrl+C para salir)...
docker compose logs -f
"@
Set-Content -Path "$DOCKER_DIR\ver-logs.bat" -Value $verLogsContent -Encoding Ascii

# Generar backup-db.bat
$backupDbContent = @"
@echo off
title Backup Base de Datos SportMedics
cls
echo ==========================================================
echo        RESPALDO BASE DE DATOS SPORTMEDICS
echo ==========================================================
echo.
echo Creando carpeta backups si no existe...
if not exist backups mkdir backups
echo.
set FECHA=%date:~6,4%-%date:~3,2%-%date:~0,2%
set HORA=%time:~0,2%-%time:~3,2%-%time:~6,2%
set HORA=%HORA: =0%
set BACKUP_FILE=backups\backup_sportmedics_%FECHA%_%HORA%.sql
echo Generando respaldo en %BACKUP_FILE%...
docker exec sportmedics-mysql mysqldump -u root -proot --all-databases > %BACKUP_FILE%
echo.
echo RESPALDO FINALIZADO.
pause
"@
Set-Content -Path "$DOCKER_DIR\backup-db.bat" -Value $backupDbContent -Encoding Ascii

# Generar restaurar-db.bat
$restaurarDbContent = @"
@echo off
title Restaurar Base de Datos SportMedics
cls
echo Archivos disponibles:
dir backups\*.sql
echo.
set /p BACKUP_FILE=Escribe el nombre del archivo exacto (ej. backup_sportmedics_...sql): 
if not exist backups\%BACKUP_FILE% (
    echo El archivo no existe.
    pause
    exit
)
echo Restaurando...
docker exec -i sportmedics-mysql mysql -u root -proot < backups\%BACKUP_FILE%
echo.
echo RESTAURACION FINALIZADA.
pause
"@
Set-Content -Path "$DOCKER_DIR\restaurar-db.bat" -Value $restaurarDbContent -Encoding Ascii

Write-Host ""
Write-Host "[5/5] Generando script Nativo..."

$arrancarNativoContent = @"
@echo off
title Arrancar Sistema Nativo - SportMedics
cls

echo ==========================================================
echo        ARRANQUE NATIVO DEL SISTEMA (SIN DOCKER)
echo ==========================================================
echo NOTA: Asegurate de tener MySQL activo en XAMPP o local (puerto 3306) con la base de datos creada.
echo.
echo [1/3] Levantando Service Registry (Eureka)...
start "Eureka Server" cmd /c "java -jar apps\service-registry.jar"
echo Esperando 15 segundos para que Eureka inicie...
timeout /t 15 /nobreak >nul

echo.
echo [2/3] Levantando Microservicios de Negocio...
start "MS Access" cmd /c "java -jar apps\ms-access.jar"
start "MS Auth" cmd /c "java -jar apps\ms-auth.jar"
start "MS Billing" cmd /c "java -jar apps\ms-billing.jar"
start "MS Employee" cmd /c "java -jar apps\ms-employee.jar"
start "MS Inventory" cmd /c "java -jar apps\ms-inventory.jar"
start "MS Member" cmd /c "java -jar apps\ms-member.jar"
start "MS Notification" cmd /c "java -jar apps\ms-notification.jar"
start "MS Scheduling" cmd /c "java -jar apps\ms-scheduling.jar"
start "MS Subscription" cmd /c "java -jar apps\ms-subscription.jar"
start "MS Workout" cmd /c "java -jar apps\ms-workout.jar"

echo Esperando 30 segundos para que los microservicios se registren en Eureka...
timeout /t 30 /nobreak >nul

echo.
echo [3/3] Levantando API Gateway...
start "API Gateway" cmd /c "java -jar apps\api-gateway.jar"

echo.
echo ==========================================================
echo SISTEMA INICIADO EXITOSAMENTE
echo ==========================================================
echo Eureka Server: http://localhost:8761
echo API Gateway: http://localhost:8080
echo.
pause
"@
Set-Content -Path "$NATIVO_DIR\arrancar-nativo.bat" -Value $arrancarNativoContent -Encoding Ascii

Write-Host ""
Write-Host "=========================================================="
Write-Host "??ENTREGABLES GENERADOS EXITOSAMENTE!" -ForegroundColor Green
Write-Host "=========================================================="
Write-Host "Comprime las carpetas $DOCKER_DIR y $NATIVO_DIR para subir a Drive."
Write-Host ""
