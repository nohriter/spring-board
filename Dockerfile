FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY ./build/libs/spring-board-0.0.1-SNAPSHOT.jar /app/spring-board.jar

CMD ["java", "-jar", "/app/spring-board.jar"]

EXPOSE 80
