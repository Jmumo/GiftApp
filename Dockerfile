# Build Stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests



FROM openjdk:17-jdk

WORKDIR /app

COPY --from=build /build/target/GiftsBackend-0.0.1-SNAPSHOT.jar /app/GiftsBackend.jar

#ENV APP_PORT = 8080

ENV DB_URL=jdbc:mysql://mysqlDB:3306/GiftDB

EXPOSE 8080

CMD ["java", "-jar","-Dspring.datasource.url=${DB_URL}", "GiftsBackend.jar"]


