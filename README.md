# Rinconcito Marino – Gestión de Mariscos

🌐 **Sitio en línea:** [https://gestion-de-mariscos.onrender.com/](https://gestion-de-mariscos.onrender.com/)

> El sitio está en el plan gratuito de Render: si estuvo un rato sin visitas, la primera carga puede tardar cerca de un minuto.

*Del mar a tu mesa, frescura garantizada.* – Lima, Perú

Aplicación web para la venta de mariscos y platos preparados, desarrollada con **Spring Boot**. Permite mostrar un catálogo de productos, armar un carrito de compras y enviar pedidos, además de un formulario de contacto y un acceso de administrador.

---

## Descripción del Proyecto

La aplicación ofrece:

- ✅ Catálogo de productos (mariscos y platos preparados)
- ✅ Carrito de compras con resumen y total
- ✅ Pedidos con datos del cliente y tipo de entrega (Delivery o Recojo en tienda)
- ✅ Formulario de contacto
- ✅ Carga inicial de productos al arrancar la aplicación
- 🚧 Login de administrador (en desarrollo)

---

## Equipo de Desarrollo

| Integrante | GitHub | Rol |
|-----------|--------|-----|
| **Angel Huaman** | [@AngelHuaman13](https://github.com/AngelHuaman13) | Líder del Proyecto |
| **Alex Cyber** | [@Alex-cyber26](https://github.com/Alex-cyber26) | Backend (Merges, Ramas) |
| **Ashi Games** | [@Ashi-Games](https://github.com/Ashi-Games) | Backend (Tags, Features) |
| **Diego (Diemox)** | [@Diemox](https://github.com/Diemox) | Backend (Releases, Issues) |
| **Lufer** | [@lufer-s](https://github.com/lufer-s) | Backend (Branches, Releases) |

---

## Tecnologías Utilizadas

### Backend
- **Java 21**
- **Spring Boot 3.5** – Framework principal
- **Spring Data JPA** – Persistencia de datos
- **Spring Security** – Autenticación y autorización
- **JWT (jjwt)** – Tokens de sesión
- **Springdoc OpenAPI** – Documentación de la API
- **OpenPDF** – Generación de PDF
- **Lombok** – Reducción de código repetitivo

### Frontend
- HTML5, CSS3 y JavaScript
- Bootstrap 5
- Thymeleaf (plantillas)

### Base de datos
- **MySQL 8.4** (con Docker Compose)
- **H2** en memoria (en el despliegue de Render)

### Herramientas
- Maven – Gestor de dependencias
- Docker y Docker Compose – Contenedores
- GitHub Actions – Automatización
- Render – Despliegue
- Git y GitHub – Control de versiones

---

## Despliegue en Render

El proyecto está publicado en Render como un **Web Service con Docker**:

- **Rama:** `dev`
- **Directorio raíz:** `backend/gestionhotelesbackend`
- **Dockerfile:** `./Dockerfile`
- **Base de datos:** H2 en memoria, configurada con variables de entorno (`SPRING_DATASOURCE_*`). Los productos se cargan al iniciar; los pedidos y usuarios nuevos se pierden cuando el servicio se reinicia.
- **Puerto:** `PORT=8080`

---

## Requisitos Previos

Para ejecutar con **Docker** (recomendado):

- **Docker Desktop**
  ```bash
  docker --version
  ```

Para ejecutar **sin Docker**:

- **Java JDK 21**
  ```bash
  java -version
  ```
- **Maven 3.9 o superior**
  ```bash
  mvn -version
  ```

---

## Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/AngelHuaman13/Gestion-de-Mariscos.git
cd Gestion-de-Mariscos
git checkout dev
```

### 2. Opción A: con Docker Compose (recomendada)

Desde la raíz del proyecto:
```bash
docker compose -f docker-compose.mariscos.yaml up --build
```

Esto levanta dos contenedores:

| Servicio | Contenedor | Descripción |
|----------|-----------|-------------|
| `db` | `gestion-mysql` | Base de datos MySQL 8.4 con volumen para los datos |
| `backend` | `gestion-hoteles` | Aplicación Spring Boot, construida con el `Dockerfile` |

La aplicación estará disponible en: `http://localhost:8080`

Para detenerla:
```bash
docker compose -f docker-compose.mariscos.yaml down
```

Para borrar también los datos de la base:
```bash
docker compose -f docker-compose.mariscos.yaml down -v
```

### 3. Opción B: sin Docker
```bash
cd backend/gestionhotelesbackend
mvn clean package -DskipTests
java -jar target/*.jar
```

Esta opción requiere una base de datos configurada en `application.properties`.

---

## Docker

### Dockerfile
Ubicado en `backend/gestionhotelesbackend/Dockerfile`. Construye la imagen en dos etapas:

1. **Compilación:** imagen `maven:3.9-eclipse-temurin-21` que genera el `.jar`.
2. **Ejecución:** imagen liviana `eclipse-temurin:21-jre` que ejecuta la aplicación en el puerto 8080.

### docker-compose.mariscos.yaml
Ubicado en la raíz del proyecto. Define:

- **`db`:** MySQL con un *healthcheck* para confirmar que está listo antes de arrancar el backend.
- **`backend`:** se construye con el `Dockerfile`, espera a que `db` esté saludable y se conecta a ella mediante variables de entorno (`SPRING_DATASOURCE_*`).

---

## GitHub Actions

Los workflows están en `.github/workflows/`. Cada integrante tiene su propia acción:

| # | Integrante | Workflow | Qué practica |
|---|-----------|----------|--------------|
| 1 | Angel Huaman | `accion_1_basico_commits.yml` | Comandos básicos, configuración de Git, commit y push automáticos |
| 2 | Lufer | `accion_2_condicionales_if_bucle_for.yml` | Condicionales y bucles |
| 3 | Alex Cyber | `accion_3_varios_jobs.yml` | Varios jobs en Ubuntu y Windows |
| 4 | Diego (Diemox) | `accion_4_matriz_os_version.yml` | Matriz de sistemas operativos y versiones |
| 5 | Ashi Games | `accion_5_needs_cache.yml` | Jobs encadenados con `needs` y uso de caché |

Cada workflow se ejecuta desde la pestaña **Actions → Run workflow**. La acción 1 deja un registro de cada ejecución en `docs/registro-actions.txt`.

---

## Estructura del Proyecto

```
Gestion-de-Mariscos/
├── .github/workflows/             # Acciones de GitHub Actions
├── backend/gestionhotelesbackend/
│   ├── src/main/
│   │   ├── java/com/gestionhoteles/backend/
│   │   │   ├── config/            # Configuración (CORS, seguridad, datos iniciales)
│   │   │   ├── controller/        # Controladores
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── entity/            # Entidades JPA
│   │   │   ├── repository/        # Acceso a datos
│   │   │   └── service/           # Lógica de negocio
│   │   └── resources/
│   │       ├── templates/         # HTML (Thymeleaf)
│   │       ├── static/            # CSS, JS, imágenes
│   │       └── application.properties
│   ├── Dockerfile
│   └── pom.xml
├── database/                      # Scripts de base de datos
├── docs/                          # Registro de la acción 1
├── docker-compose.mariscos.yaml
└── README.md
```

---

## Flujo de Trabajo

- **`main`:** rama estable.
- **`dev`:** rama de integración del equipo.
- **`feature/nombre`:** rama de trabajo de cada integrante.

Los cambios se proponen con **Pull Request** hacia `dev` y se revisan antes de unirse.

1. Crear una rama: `git checkout -b feature/mi-rama`
2. Hacer commit: `git commit -m "Descripción del cambio"`
3. Subir la rama: `git push origin feature/mi-rama`
4. Abrir un Pull Request y pedir revisión a un compañero

---

## Contacto

**Líder del Proyecto:** Angel Oscar Moscoso Huaman
**GitHub:** [@AngelHuaman13](https://github.com/AngelHuaman13)

---

## Recursos Útiles

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Documentación de Docker](https://docs.docker.com/)
- [Documentación de GitHub Actions](https://docs.github.com/actions)
- [Documentación de Render](https://render.com/docs)
