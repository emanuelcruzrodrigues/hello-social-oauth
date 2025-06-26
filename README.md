# Hello Social OAuth

A Spring Boot application that enables OAuth2 client authentication with a user-friendly web interface and support for MongoDB or in-memory data storage.

## ✨ Features

- 🔐 OAuth2 client authentication with Spring Security
- 🌐 Web interface using Thymeleaf templates
- 🗄️ Optional MongoDB integration or in-memory storage
- 🧩 Thymeleaf Layout Dialect for reusable layouts
- 📊 Structured logging with Logstash encoder

## 🛠️ Requirements

- Java 21
- Gradle
- MongoDB instance (optional)

---

## 🚀 Getting Started

### Option A: Run with Docker

You can run the app directly from Docker Hub:

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=common,local \
  -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=your-client-id \
  -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=your-client-secret \
  emanuelrodrigues/hello-social-oauth
````

Or mount a local config file:

```bash
docker run -p 8080:8080 \
  -v $(pwd)/application-local.properties:/app/config/application-local.properties \
  -e SPRING_CONFIG_ADDITIONAL_LOCATION=file:/app/config/ \
  emanuelrodrigues/hello-social-oauth
```

> 📦 Docker Hub: [emanuelrodrigues/hello-social-oauth](https://hub.docker.com/repository/docker/emanuelrodrigues/hello-social-oauth/general)

---

### Option B: Run Locally from Source

#### 1. Clone the Repository

```bash
git clone <REPOSITORY_URL>
cd hello-social-oauth
```

#### 2. Configure Application Properties

The app loads the profiles `common,local` by default.
Edit the file:

```bash
src/main/resources/application-local.properties
```

* Data is stored in memory by default
* Only Google is enabled initially
* For other providers, check:

  * `application-render.yml`
  * `application-ssl.yml`

##### Google Setup Guide

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a project and OAuth Client ID (Web Application)
3. Add:

   * JavaScript Origin: `http://localhost:8080`
   * Redirect URI: `http://localhost:8080/login/oauth2/code/google`
4. Update `application-local.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
```

---

### 3. Running the App

#### IDE

* Open in IntelliJ/Eclipse
* Run: `com.emanuel.hello.HelloSocialOauthApplication`

#### CLI

```bash
./gradlew build
./gradlew bootRun
# or
java -jar build/libs/hello-social-oauth.jar
```

---

## 🔗 OAuth Provider Dashboards

| Provider  | Link                                                                      |
| --------- | ------------------------------------------------------------------------- |
| Google    | [console.cloud.google.com](https://console.cloud.google.com/auth/clients) |
| GitHub    | [github.com/settings/developers](https://github.com/settings/developers)  |
| Facebook  | [developers.facebook.com](https://developers.facebook.com/)               |
| Microsoft | [portal.azure.com](https://portal.azure.com/)                             |
| Auth0     | [manage.auth0.com](https://manage.auth0.com/dashboard)                    |
| Steam     | [steamcommunity.com/dev/apikey](https://steamcommunity.com/dev/apikey)    |

