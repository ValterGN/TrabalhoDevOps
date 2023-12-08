FROM maven:3.8.6-ibm-semeru-17-focal as builder
WORKDIR /app
COPY . .
RUN mvn clean install -Dmaven.test.skip

FROM openjdk:17-buster
WORKDIR /app
EXPOSE 8080
COPY --from=builder "/app/target/demo-0.0.1-SNAPSHOT.jar" "app.jar"
ENTRYPOINT ["java","-jar","app.jar"]

