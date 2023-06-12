# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk
WORKDIR /app
RUN apt update && \
    apt install -y git python3 python3-pip
COPY ["pom.xml", "mvnw", "./"]
COPY .mvn .mvn
RUN ./mvnw install -Dspring-boot.repackage.skip=true
RUN pip3 install pandas
RUN apt install -y gfortran libopenblas-dev liblapack-dev cmake
RUN pip3 install meson
RUN pip3 install scikit-learn
COPY . .
RUN ./mvnw package
CMD ["java", "-jar", "target/spring-0.0.1-SNAPSHOT.jar"]
EXPOSE 8085
