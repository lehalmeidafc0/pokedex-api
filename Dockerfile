# Dockerfile usado pelo Render para gerar e rodar a API.
# Mesma ideia do slide (Eclipse Temurin 21), mas em 2 etapas:
# 1) uma imagem com Maven compila o projeto e gera o .jar
# 2) uma imagem leve só com o Java roda o .jar
# Assim não dependemos do arquivo mvnw estar no repositório.

# ---------- Etapa 1: Build ----------
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests clean package

# ---------- Etapa 2: Execução ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/pokedex-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "java -jar app.jar"]
