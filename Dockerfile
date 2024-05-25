# İlk aşama: Maven ile derleme
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean compile package

# İkinci aşama: Derlenmiş uygulamayı çalıştırma
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/was-backend.jar ./aws-deploy.jar
EXPOSE 8080
CMD ["java", "-jar", "aws-deploy.jar"]
