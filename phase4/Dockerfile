# syntax=docker/dockerfile:1
ARG JAVA_VERSION=17

FROM eclipse-temurin:${JAVA_VERSION}-jre AS base
WORKDIR /app

FROM base as build
COPY --chown=root:root build/libs/*.jar ./app.jar
RUN ["java", "-Djarmode=layertools", "-jar", "app.jar", "extract"]

FROM base as run
COPY --chown=root:root --from=build /app/dependencies/ ./
COPY --chown=root:root --from=build /app/spring-boot-loader/ ./
COPY --chown=root:root --from=build /app/snapshot-dependencies/ ./
COPY --chown=root:root --from=build /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
