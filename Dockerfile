FROM eclipse-temurin:21-jdk

ARG JAR_FILE=build/libs/hello-social-oauth.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT ["java", "-jar", "/app.jar"]