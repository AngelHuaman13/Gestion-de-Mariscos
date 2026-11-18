# =====================================================================
#  Frontend - Rinconcito Marino (Nginx)
#
#  Nginx sirve directamente los archivos estaticos del sitio (CSS, JS,
#  imagenes) y reenvia al backend las paginas Thymeleaf (/, /login, ...)
#  y la API REST (/api/...).
#
#  El contexto de build es la RAIZ del repositorio, porque los archivos
#  estaticos estan dentro del proyecto Spring Boot:
#    docker build -f frontend/Dockerfile -t gestion-mariscos-frontend:1.0 .
#
#  Ejecutar (el backend debe estar en la misma red con el nombre "backend"):
#    docker run --rm -p 3000:80 --network <red> gestion-mariscos-frontend:1.0
# =====================================================================
FROM nginx:1.30-alpine

# Direccion del backend (sin "/" al final). Por defecto es el servicio
# "backend" en el puerto 8080, como en Docker Compose y en Kubernetes.
ENV BACKEND_URL=http://backend:8080

# Al arrancar, la imagen oficial de Nginx procesa las plantillas de
# /etc/nginx/templates y reemplaza ${BACKEND_URL} por su valor.
RUN rm -f /etc/nginx/conf.d/default.conf /usr/share/nginx/html/*
COPY frontend/nginx/default.conf.template /etc/nginx/templates/default.conf.template

# Archivos estaticos: los mismos que empaqueta Spring Boot
COPY backend/gestionhotelesbackend/src/main/resources/static/ /usr/share/nginx/html/

EXPOSE 80

# El contenedor queda "healthy" cuando Nginx responde
HEALTHCHECK --interval=15s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -q -O /dev/null http://127.0.0.1/healthz || exit 1
