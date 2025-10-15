# ===== Build Stage =====
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
COPY src /app/src
RUN chmod +x gradlew && ./gradlew clean build -x test

# ===== Runtime Stage =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 앱 JAR 및 OTel agent 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar
COPY opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app/app.jar"]
