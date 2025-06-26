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
2. **Configure application properties:**
   - Edit `src/main/resources/application-local.properties` to set up your OAuth2 providers.

3. **Build the project:**
```
./gradlew build
```
4. **Run the application:**
```
./gradlew bootRun
```
5. **Or run the jar:**
```
java -jar build/libs/hello-social-oauth.jar
```
## License

This project is licensed under the MIT License.
