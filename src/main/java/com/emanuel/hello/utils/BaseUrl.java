package com.emanuel.hello.utils;

import jakarta.servlet.http.HttpServletRequest;

public class BaseUrl {
    public static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme(); // e.g., "http" or "https"
        String serverName = request.getServerName(); // e.g., "localhost" or "example.com"
        int serverPort = request.getServerPort(); // e.g., 8080 or 80

        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(scheme).append("://").append(serverName);

        if ((scheme.equalsIgnoreCase("http") && serverPort != 80) ||
                (scheme.equalsIgnoreCase("https") && serverPort != 443)) {
            baseUrl.append(":").append(serverPort);
        }

        baseUrl.append("/");

        return baseUrl.toString();
    }
}
