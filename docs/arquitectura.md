# Arquitectura – Rinconcito Marino

Responsable: Alex · rama `feature/alex`

## 1. Componentes

| Componente | Tecnología | Puerto | Función |
|------------|------------|--------|---------|
| Frontend | Nginx 1.30 (alpine) | 80 (3000 en local) | Sirve `css/`, `js/`, `img/`, `icons/` y reenvía páginas y `/api` al backend |
| Backend | Spring Boot 3.5 + Java 21 (Thymeleaf, JPA, Security) | 8080 | Páginas Thymeleaf y API REST (`/api/productos`, pedidos, contacto) |
| Base de datos | MySQL 8 (H2 en memoria si no se configura) | 3306 | Productos, pedidos, usuarios, mensajes de contacto |

## 2. Diagrama general

```mermaid
flowchart LR
    U([Usuario / Navegador])

    subgraph APP[Aplicación]
        F[Frontend<br/>Nginx :80]
        B[Backend<br/>Spring Boot :8080]
        D[(MySQL :3306)]
    end

    U -->|HTTP| F
    F -->|estáticos css/js/img| U
    F -->|páginas Thymeleaf y /api| B
    B -->|JPA / JDBC| D
```

## 3. Del código al despliegue

```mermaid
flowchart LR
    DEV[Desarrollador<br/>rama feature/*] -->|git push| GH[GitHub<br/>Gestion-de-Mariscos]
    GH -->|dispara| GA[GitHub Actions<br/>ci.yml: Maven + build Docker]
    GH -->|Dockerfiles| DK[Docker<br/>imágenes backend y frontend]
    DK --> K8[Kubernetes<br/>Docker Desktop]
    GH -->|deploy automático| R[Render<br/>app en línea]
```

## 4. Vista en Kubernetes (Docker Desktop)

```mermaid
flowchart TB
    subgraph K8S[Clúster Kubernetes - Docker Desktop]
        SB[Service backend-service<br/>ClusterIP :8080] --> PB[Pod backend<br/>gestion-de-mariscos-backend]
        SD[Service db<br/>ClusterIP :3306] --> PD[Pod mysql<br/>mysql:8.4]
        PB --> SD
        PD --- PVC[(PVC mysql-pvc)]
        SEC[[Secret mysql-secret]] -.-> PB
        SEC -.-> PD
    end
```

Archivos: `k8s/deployment.yaml` (PVC + Deployments `mysql` y `backend`) y `k8s/service.yaml` (Services `db` y `backend-service`).

> Pendiente de confirmar con el equipo: agregar el frontend (Deployment + Service) a `k8s/` si se quiere desplegar también Nginx en el clúster.
