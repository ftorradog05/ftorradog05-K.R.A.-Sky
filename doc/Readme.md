# ✈️ KRAsky ERP - Sistema de Gestión de Aerolínea

> Proyecto final para Desarrollo de Aplicaciones Multiplataforma (2º DAM).
> Un sistema integral que combina una **API RESTful** robusta con un **Dashboard Administrativo** estilo Enterprise.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Frontend-blue)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-purple)

## 📋 Descripción del Proyecto
Este proyecto implementa **KRAsky ERP**, una solución completa para la gestión de una aerolínea comercial. El sistema ha sido diseñado siguiendo una **arquitectura multicapa** estricta, separando la lógica de negocio, el acceso a datos y la presentación.

El sistema permite la gestión integral de:
* **Flota Aérea:** Control de aviones y capacidades.
* **Programación de Vuelos:** Rutas, horarios y estados.
* **Base de Datos de Pasajeros:** Gestión de clientes (CRM).
* **Emisión de Billetes:** Sistema transaccional de reservas con validaciones complejas.

---

## 🚀 Instalación y Puesta en Marcha

Sigue estos pasos para desplegar el proyecto en tu entorno local:

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/krasky-erp.git](https://github.com/tu-usuario/krasky-erp.git)
    ```
2.  **Base de Datos:**
    * Crea una base de datos en MySQL llamada `krasky`.
    * Configura tu usuario y contraseña en `src/main/resources/application.properties`.
3.  **Carga de Datos (Scripts SQL):**
    * [cite_start]Ejecuta los scripts proporcionados en la carpeta `/sql` para generar las tablas y cargar los datos de prueba (Boeing, Airbus, Vuelos de prueba, etc.)[cite: 433].
4.  **Ejecución:**
    * Ejecuta la clase principal `KraSkyApplication.java`.
    * Acceso Web: `http://localhost:8080/`
    * Acceso API: `http://localhost:8080/api/`

---

## 📦 Módulos del Sistema (Documentación Visual y Técnica)

A continuación se detalla cada módulo funcional, mostrando la integración entre el Backend (API) y el Frontend (Web).

<details>
<summary><strong>📊 1. Dashboard Principal (Panel de Control)</strong></summary>

### Interfaz Web (Thymeleaf + Bootstrap 5)
El punto de entrada es un **Dashboard estilo ERP** profesional. Incluye una barra lateral fija y tarjetas KPI (Key Performance Indicators) que muestran el estado de la aerolínea de un vistazo.

![Dashboard Principal](doc/ejemplo.png)

### Detalles Técnicos
* **Controlador:** `HomeController.java`
* **Diseño:** Implementación de Sidebar fijo con CSS personalizado y `fragmentos` de Thymeleaf para reutilizar el menú en todas las páginas.
</details>

<details>
<summary><strong>✈️ 2. Gestión de Vuelos (Operaciones)</strong></summary>

Módulo encargado de la programación de rutas y asignación de aeronaves.

### 1. Vista Web
Tabla interactiva que muestra los vuelos programados. [cite_start]Incluye lógica visual para mostrar **Badges de Estado** con diferentes colores (Programado = Azul, Cancelado = Rojo, etc.)[cite: 425].

![Lista de Vuelos](doc/ejemplo.png)

### 2. Prueba API REST (Hoppscotch)
Endpoint: `GET /api/vuelos`
[cite_start]La API devuelve objetos **DTO** (`VueloDTO`) en lugar de entidades para evitar bucles infinitos y proteger la estructura interna de la base de datos[cite: 179].

![Prueba API Vuelos](doc/ejemplo.png)

### 3. Lógica de Negocio
* [cite_start]**Relación:** Implementación de `@ManyToOne` donde múltiples vuelos pertenecen a un único avión[cite: 127].
* **Validación:** Se impide la eliminación de vuelos si tienen reservas activas.
</details>

<details>
<summary><strong>🎫 3. Reservas y Billetes (Núcleo Transaccional)</strong></summary>

Este es el módulo más complejo, encargado de vincular Pasajeros con Vuelos y generar la facturación.

### 1. Interfaz Web
Formulario de emisión de billetes que valida la disponibilidad en tiempo real.

![Formulario Reservas](doc/ejemplo.png)

### 2. Lógica de Negocio (Service Layer)
[cite_start]El servicio `ReservaServiceImpl` implementa validaciones críticas[cite: 300]:
1.  Verifica que el vuelo y el pasajero existen.
2.  Calcula el `precioTotal` automáticamente basándose en la clase (Business/Turista).
3.  Genera un **Código de Reserva Único** (`SKY...`).

### 3. Prueba API REST
Endpoint: `POST /api/reservas`
Ejemplo de creación de una reserva mediante JSON. El sistema devuelve `201 Created` si la operación es exitosa.

![Postman Reserva](doc/ejemplo.png)
</details>

<details>
<summary><strong>👥 4. Recursos: Pasajeros y Flota</strong></summary>

Gestión de las entidades maestras del sistema.

### Pasajeros (CRM)
Base de datos de clientes con validación de **DNI único** y formato de email.
![Lista Pasajeros](doc/ejemplo.png)

### Flota de Aviones
Gestión de inventario. [cite_start]Implementa control de **Integridad Referencial**: el sistema captura la excepción si intentas borrar un avión que tiene vuelos asignados y muestra una alerta amigable al usuario en lugar de un error 500[cite: 312].
![Lista Aviones](doc/ejemplo.png)
</details>

---

## ✅ Tabla de Cumplimiento de Requisitos

Este proyecto cubre el 100% de los requisitos especificados en el enunciado del Proyecto DAM:

| Fase PDF | Requisito | Estado | Ubicación Principal en Código |
| :--- | :--- | :---: | :--- |
| **Fase 1** | Configuración (Maven, MySQL, Estructura) | ✅ | [cite_start]`pom.xml`, `application.properties` [cite: 47] |
| **Fase 2** | Modelo de Datos (Entidades, Relaciones, Enums) | ✅ | [cite_start]`com.krasky.model` (4 Entidades) [cite: 98] |
| **Fase 3** | DTOs (Separación de capas) | ✅ | [cite_start]`com.krasky.dto` (4 DTOs) [cite: 178] |
| **Fase 4** | Repositorios (JPA y @Query) | ✅ | [cite_start]`com.krasky.repository` [cite: 213] |
| **Fase 5** | Servicios (Lógica de Negocio) | ✅ | [cite_start]`com.krasky.service.impl` [cite: 270] |
| **Fase 6** | API REST (Controladores y Endpoints) | ✅ | [cite_start]`com.krasky.controller.rest` [cite: 330] |
| **Fase 7** | Interfaz Web (Thymeleaf + Bootstrap) | ✅ | [cite_start]`templates/`, `controller.web` [cite: 371] |
| **Fase 8** | Scripts SQL y Datos de Prueba | ✅ | [cite_start]`/sql` (Carga inicial) [cite: 433] |
| **Extra** | Manejo de Excepciones Global | ✅ | [cite_start]`GlobalExceptionHandler.java` [cite: 317] |

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17+
* **Framework Backend:** Spring Boot 3 (Spring MVC, Spring Data JPA)
* **Motor de Plantillas:** Thymeleaf
* **Diseño UI:** Bootstrap 5 (Estilo Enterprise)
* **Base de Datos:** MySQL
* **Herramientas:** Maven, Lombok, IntelliJ IDEA

---

### ✒️ Autores
**Equipo KRAsky - 2º DAM**
* [Tu Nombre] - Backend & API
* [Nombre Compañero 1] - Frontend & Diseño
* [Nombre Compañero 2] - Base de Datos & QA