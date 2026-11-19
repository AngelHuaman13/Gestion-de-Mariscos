# =====================================================================
#  Backend - Rinconcito Marino (Spring Boot 3.5 + Java 21)
#
#  Imagen en dos etapas:
#    1) build   -> compila el proyecto con Maven y genera el .jar
#    2) runtime -> solo el JRE y el .jar (imagen final mas liviana)
#
#  Construir (desde la raiz del repositorio):
#    docker build -t gestion-mariscos-backend:1.0 backend/gestionhotelesbackend
#
#  Ejecutar (sin configurar nada usa H2 en memoria):
#    docker run --rm -p 8080:8080 gestion-mariscos-backend:1.0
#
#  Render usa este mismo Dockerfile
#  (Root Directory: backend/gestionhotelesbackend, Dockerfile: ./Dockerfile).
# =====================================================================

# ---------- Etapa 1: compilacion ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Primero solo el pom.xml: las dependencias quedan en una capa en cache
# y no se vuelven a descargar cuando solo cambia el codigo fuente.
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp package -DskipTests

# ---------- Etapa 2: ejecucion ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Usuario sin privilegios: la aplicacion no corre como root
RUN groupadd --system spring \
 && useradd --system --gid spring --no-create-home --shell /usr/sbin/nologin spring

COPY --from=build /app/target/*.jar app.jar

# Base de datos por defecto: H2 en memoria (igual que en Render), asi la
# imagen arranca sin MySQL. Para usar MySQL se sobrescriben las variables
# SPRING_DATASOURCE_URL, _USERNAME y _PASSWORD (ver docker-compose.mariscos.yaml).
ENV SPRING_DATASOURCE_URL="jdbc:h2:mem:mariscos;DB_CLOSE_DELAY=-1"

USER spring
EXPOSE 8080

# El contenedor queda "healthy" cuando la API responde
HEALTHCHECK --interval=15s --timeout=5s --start-period=60s --retries=5 \
  CMD curl -fsS -o /dev/null http://localhost:8080/api/productos || exit 1

CMD ["java", "-jar", "/app/app.jar"]
