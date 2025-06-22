package com.emanuel.hello.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HcpSecretsEnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String clientId = System.getenv("HCP_CLIENT_ID");
        String clientSecret = System.getenv("HCP_CLIENT_SECRET");
        String orgId = System.getenv("HCP_ORG_ID");
        String projectId = System.getenv("HCP_PROJECT_ID");
        String appName = System.getenv("HCP_APP_NAME");

        if (clientId == null || clientSecret == null) {
            log.error("HCP_CLIENT_ID or HCP_CLIENT_SECRET environment variables are missing.");
            return;
        }

        String accessToken = getAccessToken(clientId, clientSecret);

        if (accessToken == null) {
            log.error("Failed to obtain HCP API token. Skipping HCP secrets loading.");
            return;
        }

        Map<String, Object> secrets = fetchHcpSecrets(accessToken, orgId, projectId, appName);

        if (secrets != null && !secrets.isEmpty()) {
            MapPropertySource propertySource = new MapPropertySource("hcp-secrets", secrets);
            applicationContext.getEnvironment().getPropertySources().addFirst(propertySource);
        }
    }

    private String getAccessToken(String clientId, String clientSecret) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://auth.idp.hashicorp.com/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format(
                "client_id=%s&client_secret=%s&grant_type=client_credentials&audience=https://api.hashicorp.cloud",
                clientId, clientSecret
        );

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("access_token")) {
                return (String) responseBody.get("access_token");
            } else {
                log.error("Invalid response from HCP IdP: {}", responseBody);
            }

        } catch (Exception e) {
            log.error("Error obtaining HCP API token: {}", e.getMessage());
        }
        return null;
    }

    private Map<String, Object> fetchHcpSecrets(String accessToken, String orgId, String projectId, String appName) {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = String.format(
                "https://api.cloud.hashicorp.com/secrets/2023-11-28/organizations/%s/projects/%s/apps/%s/secrets:open",
                orgId, projectId, appName);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, Object> allSecrets = new HashMap<>();
        String pageToken = null;

        do {
            String url = baseUrl + "?pagination.page_size=100";
            if (pageToken != null) {
                url += "&pagination.next_page_token=" + pageToken;
            }

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("secrets")) {
                List<HashMap<String, Object>> secrets = (List<HashMap<String, Object>>) body.get("secrets");

                secrets.forEach(map -> {
                    String secretKey = map.get("name").toString();
                    String secretValue = ((Map<String, String>) map.get("static_version")).get("value");
                    allSecrets.put(secretKey, secretValue);
                });
            }

            // Read next_page_token
            pageToken = body != null && body.containsKey("pagination")
                    ? ((Map<String,String>) body.get("pagination")).get("next_page_token")
                    : null;
        } while (StringUtils.isNotBlank(pageToken));

        return allSecrets;
    }

}

