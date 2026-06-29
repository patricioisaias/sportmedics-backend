-- =========================================================
-- SPORTMEDICS - SCRIPT DE BASES DE DATOS
-- Sistema de microservicios para gimnasio
-- MySQL / XAMPP
-- =========================================================

-- =========================================================
-- CREACIÃ“N DE BASES DE DATOS
-- =========================================================

CREATE DATABASE IF NOT EXISTS gym_access_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_auth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_billing_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_employee_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_inventory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_member_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_notification_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_scheduling_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_subscription_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS gym_workout_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


-- =========================================================
-- CREACIÃ“N DE TABLAS POR MICROSERVICIO
-- =========================================================

-- ---------------------------------------------------------
-- BD ACCESS
-- ---------------------------------------------------------
USE gym_access_db;

CREATE TABLE IF NOT EXISTS accesses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    access_date_time DATETIME NOT NULL,
    granted BOOLEAN NOT NULL,
    denial_reason VARCHAR(255)
);

-- ---------------------------------------------------------
-- BD AUTH
-- ---------------------------------------------------------
USE gym_auth_db;

CREATE TABLE IF NOT EXISTS credentials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------
-- BD BILLING
-- ---------------------------------------------------------
USE gym_billing_db;

CREATE TABLE IF NOT EXISTS billings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- ---------------------------------------------------------
-- BD EMPLOYEE
-- ---------------------------------------------------------
USE gym_employee_db;

CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------
-- BD INVENTORY
-- ---------------------------------------------------------
USE gym_inventory_db;

CREATE TABLE IF NOT EXISTS inventory_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    location VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------
-- BD MEMBER
-- ---------------------------------------------------------
USE gym_member_db;

CREATE TABLE IF NOT EXISTS members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------
-- BD NOTIFICATION
-- ---------------------------------------------------------
USE gym_notification_db;

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipient_email VARCHAR(100) NOT NULL,
    subject VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    sent_at DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- ---------------------------------------------------------
-- BD SCHEDULING
-- ---------------------------------------------------------
USE gym_scheduling_db;

CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    activity_name VARCHAR(150) NOT NULL,
    scheduled_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- ---------------------------------------------------------
-- BD SUBSCRIPTION
-- ---------------------------------------------------------
USE gym_subscription_db;

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    duration_months INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------
-- BD WORKOUT
-- ---------------------------------------------------------
USE gym_workout_db;

CREATE TABLE IF NOT EXISTS workouts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS workout_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workout_id BIGINT NOT NULL,
    exercise_name VARCHAR(100) NOT NULL,
    muscle_group VARCHAR(50) NOT NULL,
    sets INT NOT NULL,
    reps INT NOT NULL,
    rest_seconds INT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL
);


-- =========================================================
-- DATOS DE PRUEBA INICIALES
-- =========================================================

-- ---------------------------------------------------------
-- BD ACCESS
-- ---------------------------------------------------------
USE gym_access_db;

INSERT IGNORE INTO accesses (member_id, access_date_time, granted, denial_reason) VALUES 
(1, '2026-05-17 08:30:00', true, NULL),
(2, '2026-05-17 09:15:00', false, 'MEMBRESIA EXPIRED O MOROSO'),
(3, '2026-05-17 10:00:00', true, NULL);

-- ---------------------------------------------------------
-- BD AUTH
-- ---------------------------------------------------------
USE gym_auth_db;

INSERT IGNORE INTO credentials (username, password_hash, role, active) VALUES 
('daedo@correo.cl', '$2a$10$xyzSimulatedHashAdmin123', 'ADMIN', true),
('pcespedes@correo.cl', '$2a$10$xyzSimulatedHashUser456', 'USER', true);

-- ---------------------------------------------------------
-- BD BILLING
-- ---------------------------------------------------------
USE gym_billing_db;

INSERT IGNORE INTO billings (member_id, subscription_id, issue_date, due_date, total_amount, status) VALUES 
(1, 3, '2026-05-01', '2026-05-10', 200000, 'PAID'),
(2, 1, '2026-05-15', '2026-05-25', 25000, 'PENDING');

-- ---------------------------------------------------------
-- BD EMPLOYEE
-- ---------------------------------------------------------
USE gym_employee_db;

INSERT IGNORE INTO employees (rut, first_name, last_name, email, role, active) VALUES 
('15111222-3', 'Mauricio', 'GonzÃ¡lez', 'mgonzalez@sportmedics.cl', 'ADMIN', true),
('17444555-6', 'Carla', 'PÃ©rez', 'cperez@sportmedics.cl', 'TEACHER', true),
('19888999-0', 'Luis', 'SÃ¡nchez', 'lsanchez@sportmedics.cl', 'RECEPTIONIST', true);

-- ---------------------------------------------------------
-- BD INVENTORY
-- ---------------------------------------------------------
USE gym_inventory_db;

INSERT IGNORE INTO inventory_items (name, category, quantity, location, active) VALUES 
('Cinta de Correr Pro', 'MACHINES', 5, 'Zona Cardio', true),
('Mancuernas 10kg', 'WEIGHTS', 12, 'Zona Peso Libre', true),
('Colchoneta Yoga', 'ACCESSORIES', 20, 'Sala Clases Grupales', true);

-- ---------------------------------------------------------
-- BD MEMBER
-- ---------------------------------------------------------
USE gym_member_db;

INSERT IGNORE INTO members (rut, first_name, last_name, email, phone, active) VALUES 
('19123456-7', 'Daniel', 'Aedo', 'daedo@correo.cl', '+56912345678', true),
('19765432-1', 'Patricio', 'CÃ©spedes', 'pcespedes@correo.cl', '+56987654321', true),
('20111222-3', 'Angelo', 'Ponce', 'aponce@correo.cl', '+56911223344', true);

-- ---------------------------------------------------------
-- BD NOTIFICATION
-- ---------------------------------------------------------
USE gym_notification_db;

INSERT IGNORE INTO notifications (recipient_email, subject, message, sent_at, status) VALUES 
('daedo@correo.cl', 'Bienvenido a Sportmedics', 'Tu cuenta ha sido creada exitosamente.', '2026-05-17 10:00:00', 'SENT'),
('pcespedes@correo.cl', 'Aviso de Vencimiento', 'Tu plan vence en 3 dÃ­as.', '2026-05-17 11:30:00', 'SENT');

-- ---------------------------------------------------------
-- BD SCHEDULING
-- ---------------------------------------------------------
USE gym_scheduling_db;

INSERT IGNORE INTO appointments (member_id, employee_id, activity_name, scheduled_date, status) VALUES 
(1, 2, 'EvaluaciÃ³n FÃ­sica Inicial', '2026-06-15 10:00:00', 'SCHEDULED'),
(2, 2, 'Clase de Spinning', '2026-06-15 18:30:00', 'SCHEDULED'),
(3, 2, 'Entrenamiento Personalizado', '2026-06-16 09:00:00', 'SCHEDULED');

-- ---------------------------------------------------------
-- BD SUBSCRIPTION
-- ---------------------------------------------------------
USE gym_subscription_db;

INSERT IGNORE INTO subscriptions (name, price, duration_months, active) VALUES 
('Plan Mensual BÃ¡sico', 25000, 1, true),
('Plan Trimestral Pro', 65000, 3, true),
('Plan Anual VIP', 200000, 12, true);

-- ---------------------------------------------------------
-- BD WORKOUT
-- ---------------------------------------------------------
USE gym_workout_db;

INSERT IGNORE INTO workouts (member_id, teacher_id, name, start_date, end_date, active) VALUES 
(1, 2, 'Hipertrofia Fase 1', '2026-05-17', '2026-06-17', true);

INSERT IGNORE INTO workout_details (workout_id, exercise_name, muscle_group, sets, reps, rest_seconds, day_of_week) VALUES 
(1, 'Press de Banca', 'Pecho', 4, 10, 90, 'LUNES'),
(1, 'Sentadilla Libre', 'Piernas', 4, 12, 120, 'MIERCOLES');
