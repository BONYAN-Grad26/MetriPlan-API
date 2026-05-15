FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY ./pom.xml .

COPY ./.env .

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS final

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/.env .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

