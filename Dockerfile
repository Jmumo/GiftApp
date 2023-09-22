FROM openjdk:17-jdk

WORKDIR ./app

COPY GiftApp/target/GiftsBackend-0.0.1-SNAPSHOT.jar ./app/GiftsBackend.jar

#ENV APP_PORT = 8080

EXPOSE 8080

CMD ["java", "-jar", "GiftsBackend.jar"]


