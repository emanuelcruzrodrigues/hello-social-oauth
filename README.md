# Hello Social OAuth

A Spring Boot application providing OAuth2 client authentication, web interface, and MongoDB or in memory data storage integration.

## Features

- OAuth2 client authentication (Spring Security)
- Web interface with Thymeleaf templates
- MongoDB or in memory data storage
- Thymeleaf layout dialect for advanced templating
- Logging with Logstash encoder

## Requirements

- Java 21
- Gradle
- MongoDB instance (optional)

## Setup

1. **Clone the repository:**
   ```
   git clone <URL>
   ```
3. **Configure application properties:**
   - Edit `src/main/resources/application-local.properties` to set up your OAuth2 providers.

4. **Build the project:**
   ```
   ./gradlew build
   ```
5. **Run the application:**
  ```
   ./gradlew bootRun
  ```
6. **Or run the jar:**
  ```
   java -jar build/libs/hello-social-oauth.jar
  ```
## License

This project is licensed under the MIT License.
