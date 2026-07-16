FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --chown=spring:spring banquito-clearinghouse-service/target/*.jar app.jar

EXPOSE 8087

USER spring
ENTRYPOINT ["java", "-jar", "app.jar"]
