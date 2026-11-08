# syntax=docker/dockerfile:1

# ---- Build stage: compila todos los módulos y ejecuta las pruebas unitarias ----
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src
COPY . .
RUN mvn -B clean package

# ---- Runtime stage: solo JRE y el jar del módulo indicado ----
FROM eclipse-temurin:25-jre
# rest-input-adapter (API REST, puerto 3000) o cli-input-adapter (menú de consola)
ARG MODULE=rest-input-adapter
WORKDIR /app
RUN useradd --system --uid 10001 --no-create-home app \
 && mkdir -p /app/logs && chown -R app /app
COPY --from=build /src/${MODULE}/target/*-SNAPSHOT.jar /app/app.jar
USER app
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
