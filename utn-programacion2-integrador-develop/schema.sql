CREATE DATABASE IF NOT EXISTS foodstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE foodstore;

CREATE TABLE IF NOT EXISTS categorias (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    eliminado  BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS productos (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    precio      DOUBLE NOT NULL,
    descripcion VARCHAR(255),
    stock       INT DEFAULT 0,
    imagen      VARCHAR(255),
    disponible  BOOLEAN DEFAULT TRUE,
    eliminado   BOOLEAN DEFAULT FALSE,
    created_at  DATETIME DEFAULT NOW(),
    categoria_id BIGINT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS usuarios (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    apellido   VARCHAR(100) NOT NULL,
    mail       VARCHAR(150) NOT NULL UNIQUE,
    celular    VARCHAR(20),
    contrasena VARCHAR(255) NOT NULL,
    rol        ENUM('ADMIN','USUARIO') NOT NULL,
    eliminado  BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS pedidos (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha      DATE NOT NULL,
    estado     ENUM('PENDIENTE','CONFIRMADO','TERMINADO','CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
    total      DOUBLE DEFAULT 0,
    forma_pago ENUM('TARJETA','TRANSFERENCIA','EFECTIVO') NOT NULL,
    eliminado  BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT NOW(),
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS detalles_pedido (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad        INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    subtotal        DOUBLE NOT NULL,
    eliminado       BOOLEAN DEFAULT FALSE,
    created_at      DATETIME DEFAULT NOW(),
    producto_id     BIGINT NOT NULL,
    pedido_id       BIGINT NOT NULL,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT,
    FOREIGN KEY (pedido_id)   REFERENCES pedidos(id)   ON DELETE RESTRICT
);
