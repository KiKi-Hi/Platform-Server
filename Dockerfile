FROM gradle:7.6.1-jdk17 AS builder
WORKDIR /app
COPY . .
# Gradle Wrapper 권한 부여
RUN chmod +x ./gradlew

# Gradle 빌드 실행 (테스트 제외)
RUN ./gradlew build -x test

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/KIKIHI_BE-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

