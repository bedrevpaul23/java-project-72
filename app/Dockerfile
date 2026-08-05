FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY gradlew build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle gradle

RUN ./gradlew resolveDependencies --no-daemon

COPY src src

RUN ./gradlew clean shadowJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/build/libs/app-1.0-SNAPSHOT-all.jar app.jar

CMD ["java", "-jar", "app.jar"]
