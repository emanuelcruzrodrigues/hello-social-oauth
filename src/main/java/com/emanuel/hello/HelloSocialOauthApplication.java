package com.emanuel.hello;

import com.emanuel.hello.config.HcpSecretsEnvironmentInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(
		exclude = {
				MongoAutoConfiguration.class,
				MongoDataAutoConfiguration.class,
				MongoRepositoriesAutoConfiguration.class
		}
)
public class HelloSocialOauthApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(HelloSocialOauthApplication.class);
		app.addInitializers(new HcpSecretsEnvironmentInitializer());
		app.run(args);
	}

}
