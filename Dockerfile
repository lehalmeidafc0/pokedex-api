# Dockerfile usado no deploy.
# É a mesma ideia do slide (Java 21 da Eclipse Temurin), mas usando a imagem do Maven,
# que já vem com esse Java. Ela compila o projeto e roda o .jar, sem precisar do mvnw.

FROM maven:3.9-eclipse-temurin-21

# Create and change to the app directory.
WORKDIR /app

# Copy local code to the container image.
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src

# Build the app.
RUN mvn -B -DskipTests clean package

EXPOSE 8080

# Run the java app (as opções deixam o Java mais leve e mais rápido para subir no plano grátis)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70", "-XX:TieredStopAtLevel=1", "-XX:+UseSerialGC", "-jar", "target/pokedex-0.0.1-SNAPSHOT.jar"]
