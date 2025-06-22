package com.emanuel.hello;

import com.emanuel.hello.config.HcpSecretsEnvironmentInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloSocialOauthApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(HelloSocialOauthApplication.class);
		app.addInitializers(new HcpSecretsEnvironmentInitializer());
		app.run(args);
	}

}
