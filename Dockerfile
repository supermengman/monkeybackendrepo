# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk
WORKDIR /app
RUN apt update && \
    apt install -y git python3
COPY ["pom.xml", "mvnw", "./"]
COPY .mvn .mvn
RUN ./mvnw install -Dspring-boot.repackage.skip=true
RUN python3 -m pip install pandas
RUN python3 -m pip install sklearn
COPY . .
RUN ./mvnw package
CMD ["java", "-jar", "target/spring-0.0.1-SNAPSHOT.jar"]
EXPOSE 8085
