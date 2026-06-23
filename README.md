# Sistema de Gestión de Pedidos 🛒

Este proyecto es una aplicación de consola robusta desarrollada en **Java SE** orientada a la gestión integral de un catálogo de productos estructurado por categorías, el control de usuarios con roles definidos y la administración atómica de pedidos de compra con sus respectivos desgloses.

El sistema implementa persistencia de datos en un motor **MySQL** utilizando **JDBC nativo**, siguiendo buenas prácticas de diseño y arquitectura en capas.

## 🚀 Características Principales

- **Arquitectura en Capas Decopladas:** Separación clara de responsabilidades (`ui`, `services`, `dao`, `entities`, `config`, `enums`, `exception`).
- **Patrón Generic DAO:** Abstracción completa de las operaciones CRUD mediante interfaces parametrizadas.
- **Control de Transacciones Manual:** Garantiza la atomicidad al registrar pedidos complejos (cabecera y múltiples detalles bajo la misma transacción con control de *commit* y *rollback*).
- **Mapeo de Datos Manual (Row Mapping):** Transformación segura de tuplas relacionales a instancias orientadas a objetos, controlando valores nulos y tipos temporales (`LocalDateTime`).
- **Borrado Lógico (Soft Delete):** Implementación de bajas lógicas compartidas a través de una clase abstracta `Base`, protegiendo la integridad referencial histórica del sistema.

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java SE (JDK 17 o superior)
- **Base de Datos:** MySQL Server
- **Conectividad:** JDBC (Java Database Connectivity) con el driver oficial `com.mysql.cj.jdbc.Driver`
- **Gestión del Entorno:** Consola/Terminal Interactiva

## 📂 Estructura del Proyecto

```text
src/
├── config/       # Conectividad estática y manejo de la base de datos (DatabaseConnection)
├── dao/          # Interfases y clases de persistencia pura (Consultas SQL)
├── entities/     # Modelo de dominio del problema (Clases de negocio y contratos)
├── enums/        # Tipos estáticos para control de estados, roles y formas de pago
├── exception/    # Centralización de excepciones personalizadas (DAOException)
├── services/     # Capa intermedia para la orquestación y reglas de negocio
└── ui/           # Interfaz de usuario interactiva por consola (Punto de entrada)
## Crear la Base de Datos 🗄️
```
Para iniciar la aplicación, es necesario crear la estructura relacional en tu servidor local de MySQL. Pasos:
1. Abre tu gestor de base de datos preferido (MySQL Workbench, phpMyAdmin o la terminal de MySQL).
2. Ejecuta la siguiente sentencia para crear la base de datos:
   ```sql
   CREATE DATABASE tp10;
