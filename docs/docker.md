# Docker – Rinconcito Marino

Responsable: Diego (Diemox) · rama `feature/diego`

La aplicación se divide en dos imágenes: el **backend** (Spring Boot) y el **frontend** (Nginx).

```
Navegador ──► frontend (Nginx :80) ──► backend (Spring Boot :8080) ──► Base de datos (H2 o MySQL)
```

## Imágenes

| Imagen | Dockerfile | Contexto de build | Puerto |
|--------|------------|-------------------|--------|
| `gestion-mariscos-backend:1.0` | `backend/gestionhotelesbackend/Dockerfile` | `backend/gestionhotelesbackend` | 8080 |
| `gestion-mariscos-frontend:1.0` | `frontend/Dockerfile` | raíz del repositorio (`.`) | 80 |

### Backend (Spring Boot)

- Build en dos etapas: `maven:3.9-eclipse-temurin-21` compila el `.jar` y `eclipse-temurin:21-jre` lo ejecuta.
- Las dependencias de Maven quedan en una capa de caché: si solo cambia el código, no se vuelven a descargar.
- La aplicación corre con un usuario sin privilegios (`spring`), no como root.
- Sin configurar nada usa **H2 en memoria**, así que la imagen arranca sola. Para MySQL se sobrescriben `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` (así lo hace `docker-compose.mariscos.yaml`).
- `HEALTHCHECK`: el contenedor queda `healthy` cuando responde `GET /api/productos`.
- Render usa este mismo Dockerfile: las variables `SPRING_DATASOURCE_*` configuradas allá tienen prioridad sobre los valores por defecto de la imagen.

### Frontend (Nginx)

Las páginas del sitio son plantillas Thymeleaf, por eso las genera el backend. El contenedor del frontend:

- entrega directamente los archivos estáticos (`css/`, `js/`, `img/`, `icons/`, `admin-login.html`), que se copian de `backend/gestionhotelesbackend/src/main/resources/static/`;
- reenvía todo lo demás (las páginas y `/api/...`) al backend indicado en la variable `BACKEND_URL` (por defecto `http://backend:8080`).

La configuración de Nginx está en `frontend/nginx/default.conf.template`.

## Construir las imágenes

Desde la raíz del repositorio:

```bash
docker build -t gestion-mariscos-backend:1.0 backend/gestionhotelesbackend
```

```bash
docker build -f frontend/Dockerfile -t gestion-mariscos-frontend:1.0 .
```

O las dos a la vez con Compose:

```bash
docker compose -f docker-compose.mariscos.yaml build
```

## Ejecutar con Docker Compose

Levanta MySQL, el backend y el frontend:

```bash
docker compose -f docker-compose.mariscos.yaml up --build
```

- Frontend: <http://localhost:3000>
- Backend directo: <http://localhost:8080>

El frontend arranca cuando el backend ya está `healthy`. Para detener todo:

```bash
docker compose -f docker-compose.mariscos.yaml down
```

## Ejecutar solo con `docker run` (backend con H2)

```bash
docker network create mariscos
```

```bash
docker run -d --name backend --network mariscos gestion-mariscos-backend:1.0
```

```bash
docker run -d --name frontend --network mariscos -p 3000:80 gestion-mariscos-frontend:1.0
```

## Variables de entorno

| Imagen | Variable | Valor por defecto | Uso |
|--------|----------|-------------------|-----|
| backend | `SPRING_DATASOURCE_URL` | `jdbc:h2:mem:mariscos;DB_CLOSE_DELAY=-1` | URL de la base de datos |
| backend | `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` | los de `application.properties` | Credenciales de la base de datos (para MySQL) |
| backend | `JAVA_TOOL_OPTIONS` | — | Opciones de la JVM, por ejemplo `-XX:MaxRAMPercentage=60` |
| frontend | `BACKEND_URL` | `http://backend:8080` | Dirección del backend, sin `/` al final |

## Notas para Kubernetes

- Con el Kubernetes de Docker Desktop (clúster `kubeadm`, el que viene por defecto) no hace falta subir las imágenes a Docker Hub: basta con construirlas en la misma PC. Usar la etiqueta `1.0` (no `latest`) con `imagePullPolicy: IfNotPresent`.
- Con 1 pod del backend ya funciona toda la aplicación (usa H2 en memoria).
- Si también se despliega el frontend, el Service del backend debe llamarse `backend` y exponer el puerto `8080`; si se llama distinto, hay que cambiar `BACKEND_URL` en el frontend.
