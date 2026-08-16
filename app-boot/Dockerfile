# --- Build stage ---
# Uses the Maven image directly. If you prefer the Maven Wrapper, run
# `mvn -N io.takari:maven:wrapper` locally once to generate mvnw/.mvn/, commit them,
# then switch this stage back to a JDK base image + `./mvnw`.
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /workspace

# Copy just the POMs first so dependency resolution is its own cached layer,
# only invalidated when a pom.xml actually changes.
COPY pom.xml ./
COPY app-common/pom.xml app-common/pom.xml
COPY app-domain/pom.xml app-domain/pom.xml
COPY app-persistence/pom.xml app-persistence/pom.xml
COPY app-service/pom.xml app-service/pom.xml
COPY app-api/pom.xml app-api/pom.xml
COPY app-config/pom.xml app-config/pom.xml
COPY app-boot/pom.xml app-boot/pom.xml
RUN mvn -q -B dependency:go-offline || true

COPY . .
RUN mvn -q -B clean package -DskipTests

# Unpack into layers for better Docker layer caching between builds
RUN java -Djarmode=layertools -jar app-boot/target/enterprise-app.jar extract --destination /workspace/extracted

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app

COPY --from=build /workspace/extracted/dependencies/ ./
COPY --from=build /workspace/extracted/spring-boot-loader/ ./
COPY --from=build /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build /workspace/extracted/application/ ./

USER spring:spring

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

# Healthcheck hits the actuator liveness probe — orchestrators can also probe this directly
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s \
  CMD wget -q -O - http://localhost:8080/actuator/health/liveness || exit 1
