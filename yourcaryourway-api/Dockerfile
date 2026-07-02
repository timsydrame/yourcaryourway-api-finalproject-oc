# ---------- ÉTAPE 1 : Build (l'atelier complet) ----------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests

# ---------- ÉTAPE 2 : Runtime (la pièce minimaliste) ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/yourcaryourway-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]