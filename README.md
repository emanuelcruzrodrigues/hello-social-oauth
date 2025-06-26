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

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <REPOSITORY_URL>
cd hello-social-oauth
````

### 2. Configure Application Properties

By default, the application loads the following Spring profiles: `common,local`.
Edit the file below to configure your OAuth2 providers:

```properties
src/main/resources/application-local.properties
```

* In this configuration, **data is stored in memory**.
* Only **Google authentication** is enabled by default.
* For examples of other providers (Facebook, GitHub, etc.), refer to:

    * `application-render.yml`
    * `application-ssl.yml`

#### ✅ Setting Up Google Authentication

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project
3. Navigate to **APIs & Services > Credentials**
4. Click **Create Credentials > OAuth Client ID**
5. Select **Web application** as the type
6. Add the following URIs:

    * Authorized JavaScript origins: `http://localhost:8080`
    * Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
7. Copy the **Client ID** and **Client Secret**
8. Update your `application-local.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
```

---

### 3. Running the Application

#### Option A: Using an IDE

1. Open the project in your favorite IDE (IntelliJ, Eclipse, etc.)
2. Run the main class:

```java
com.emanuel.hello.HelloSocialOauthApplication
```

#### Option B: Using the Command Line

**Build the project:**

```bash
./gradlew build
```

**Run the application:**

```bash
./gradlew bootRun
```

**Or run the built JAR:**

```bash
java -jar build/libs/hello-social-oauth.jar
```

---

## 🔗 OAuth Provider Dashboards

Configure your OAuth2 credentials using the links below:

| Provider  | Link                                                                      |
| --------- | ------------------------------------------------------------------------- |
| Google    | [console.cloud.google.com](https://console.cloud.google.com/auth/clients) |
| GitHub    | [github.com/settings/developers](https://github.com/settings/developers)  |
| Facebook  | [developers.facebook.com](https://developers.facebook.com/)               |
| Microsoft | [portal.azure.com](https://portal.azure.com/)                             |
| Auth0     | [manage.auth0.com](https://manage.auth0.com/dashboard)                    |
| Steam     | [steamcommunity.com/dev/apikey](https://steamcommunity.com/dev/apikey)    |

