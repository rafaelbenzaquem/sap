## Builder Image
FROM maven:3.9.12-amazoncorretto-21-alpine AS builder
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

## Runner Image
FROM openjdk:26-ea-21-oraclelinux8
COPY --from=builder /usr/src/app/target/*.jar /usr/app/app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]