FROM eclipse-temurin:21-jre
WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-Dio.netty.handler.ssl.noOpenSsl=true"

RUN groupadd --system spring && useradd --system --gid spring spring

COPY --chown=spring:spring banquito-clearinghouse-service/target/*.jar app.jar

EXPOSE 8087

USER spring
ENTRYPOINT ["java", "-jar", "app.jar"]
