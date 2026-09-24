FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts Makefile ./

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src src
COPY docs docs

CMD ["./gradlew", "test", "--no-daemon"]