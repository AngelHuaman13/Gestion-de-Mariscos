# Borrador de documentación para la entrega final

## Proyecto: Rinconcito Marino — Gestión de Mariscos

Este documento describe el control de versiones, la automatización, las imágenes Docker, el despliegue local en Kubernetes con Docker Desktop y el despliegue público en Render.

## 1. Git y GitHub

Git registra los cambios del código. El repositorio del equipo está en [GitHub](https://github.com/AngelHuaman13/Gestion-de-Mariscos). El flujo documentado usa `main` para la versión estable, `dev` para integrar el trabajo y ramas `feature/` para cambios concretos. Los cambios se suben a GitHub y se integran mediante Pull Request.

Comandos habituales:

```bash
git checkout dev
git pull origin dev
git checkout -b feature/descripcion
git add .
git commit -m "Describe el cambio"
git push -u origin feature/descripcion
```

Después se abre un Pull Request hacia `dev` para revisión. No se deben subir contraseñas reales ni archivos `.env`.

## 2. GitHub Actions

Los workflows están en `.github/workflows/`. El repositorio contiene cinco ejercicios de Actions: comandos básicos, condicionales y bucles, varios jobs, matriz de sistemas operativos y jobs encadenados con caché. Se pueden revisar en GitHub, pestaña **Actions**.

En el estado actual son workflows de práctica que se ejecutan manualmente con **Run workflow**; no constituyen todavía una compilación automática de la aplicación en cada Pull Request. El archivo `docs/registro-actions.txt` conserva un registro de ejecuciones del ejercicio indicado en el README.

## 3. Docker

La aplicación tiene un backend Spring Boot (Java 21), un frontend servido por Nginx y MySQL. Las imágenes se describen en `backend/gestionhotelesbackend/Dockerfile` y `frontend/Dockerfile`. El frontend sirve los archivos estáticos y reenvía las páginas dinámicas y las llamadas `/api/` al backend.

Para probar la aplicación completa con Docker Compose, desde la raíz:

```bash
docker compose -f docker-compose.mariscos.yaml up --build
```

Abrir `http://localhost:8080`. Para detener los contenedores: `docker compose -f docker-compose.mariscos.yaml down`. Esta opción sirve para Compose; el procedimiento Kubernetes está en la siguiente sección.

## 4. Kubernetes en Docker Desktop

El directorio `k8s/` contiene los manifiestos de MySQL, backend, frontend, almacenamiento persistente y servicios. Cada Deployment solicita una réplica. Los Services `db` y `backend` son internos (`ClusterIP`) y el Service `frontend` publica el puerto `30080` (`NodePort`). El PVC conserva los datos de MySQL al recrear el Pod. El Secret incluido contiene una contraseña solo para la demostración local.

### Requisitos y preparación

1. Instalar y abrir Docker Desktop.
2. En **Settings → Kubernetes**, habilitar Kubernetes y esperar a que indique que está en ejecución.
3. Abrir PowerShell en la carpeta raíz del repositorio. Confirmar `docker version` y `kubectl version --client`.
4. Comprobar el contexto con `kubectl config current-context`. Si no es `docker-desktop`, seleccionar el contexto de Docker Desktop: `kubectl config use-context docker-desktop`.

### Construir imágenes locales

Construir las imágenes con los nombres que esperan los manifiestos. La base de datos se descarga desde Docker Hub cuando haga falta; las imágenes de la aplicación se construyen en Docker Desktop:

```powershell
docker compose -f docker-compose.mariscos.yaml build backend frontend
docker image ls gestion-mariscos-backend gestion-mariscos-frontend
```

La política `IfNotPresent` permite usar las imágenes locales disponibles en Docker Desktop y descargar imágenes públicas que todavía no estén en caché.

### Aplicar y comprobar

Desde la raíz del proyecto, ejecutar en este orden:

```powershell
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl rollout status deployment/mysql --timeout=180s
kubectl rollout status deployment/backend --timeout=180s
kubectl rollout status deployment/frontend --timeout=180s
kubectl get pods,svc,pvc
```

Esperar hasta que los tres Pods estén `Running` y `READY 1/1`, y que el PVC esté `Bound`. Abrir `http://localhost:30080`. El puerto es servido por el Service `frontend` y el tráfico dinámico pasa de Nginx al backend por el Service interno `backend`.

Si un Pod no arranca, revisar el motivo antes de volver a aplicar:

```powershell
kubectl describe pods
kubectl logs deployment/mysql
kubectl logs deployment/backend
kubectl logs deployment/frontend
```

Para detener y retirar los recursos de la aplicación:

```powershell
kubectl delete -f k8s/service.yaml
kubectl delete -f k8s/deployment.yaml
kubectl delete -f k8s/secret.yaml
```

El PVC se conserva por separado. Para borrar también la base persistida, ejecutar explícitamente `kubectl delete pvc mysql-pvc`.

### Qué se presenta para demostrar el requisito

En Docker Desktop, mostrar Kubernetes habilitado y luego ejecutar `kubectl get pods,svc`. Explicar que los Deployments administran tres Pods (base de datos, backend y frontend), que los Services conectan los componentes y publican el frontend, y que el PVC conserva la base de datos. Abrir la aplicación en `http://localhost:30080`.

## 5. Despliegue público en Render

El README del proyecto declara el sitio `https://gestion-de-mariscos.onrender.com/`, desplegado como Web Service con Docker desde la rama `dev`, usando `backend/gestionhotelesbackend` como directorio raíz. Esa ficha describe el servicio publicado, mientras que Kubernetes con Docker Desktop es el despliegue local para la demostración del curso.

Según el README, el entorno de Render usa H2 en memoria: los datos creados pueden perderse cuando el servicio se reinicia. Docker Desktop Kubernetes usa MySQL con almacenamiento persistente local. Son configuraciones distintas y la persistencia local no sincroniza datos con Render.

## 6. Relación de archivos

- `README.md`: descripción y arranque general.
- `docker-compose.mariscos.yaml`: entorno de desarrollo con MySQL y backend/frontend.
- `backend/gestionhotelesbackend/Dockerfile`: imagen del backend.
- `frontend/Dockerfile` y `frontend/nginx/default.conf.template`: frontend Nginx y proxy.
- `.github/workflows/`: prácticas de GitHub Actions.
- `k8s/deployment.yaml`, `k8s/service.yaml`, `k8s/secret.yaml`: despliegue Kubernetes.
- `kind-config.yaml`: configuración opcional de kind; no se necesita para el clúster Kubernetes de Docker Desktop.

## Nota de seguridad

`k8s/secret.yaml` usa `root` únicamente para facilitar la práctica local. No reutilizar esta contraseña en Internet o producción. En un entorno real, usar credenciales fuertes y un gestor de secretos, además de configurar persistencia y respaldos adecuados.
