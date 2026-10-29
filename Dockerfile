# Dockerfile usado no Deploy para gerar e rodar a API.
# Mesma ideia do slide (Eclipse Temurin 21): a imagem do Maven já vem com o
# Java 21 (Temurin), então ela compila o projeto e também roda o .jar.
# Assim não dependemos do arquivo mvnw estar no repositório.

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

# Run the java app (limitando a memória e acelerando o início no plano gratuito)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70", "-XX:TieredStopAtLevel=1", "-XX:+UseSerialGC", "-jar", "target/pokedex-0.0.1-SNAPSHOT.jar"]
