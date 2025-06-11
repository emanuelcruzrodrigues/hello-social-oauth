package com.emanuel.hello.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SteamService {

    @Value("${steam.api.key}")
    private String steamApiKey;

    public Map<String, Object> getPlayerSummary(String steamId) {
        String url = "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/" +
                "?key=" + steamApiKey +
                "&steamids=" + steamId;

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        response = (Map<String, Object>) response.get("response");
        return ((List<Map<String, Object>>) response.get("players")).get(0);
    }

    public List<Map<String, Object>> getPlayerListOfGames(String steamId) {
        String url = String.format("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=%s&format=json", steamApiKey, steamId);

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        response = (Map<String, Object>) response.get("response");
        return (List<Map<String, Object>>) response.get("games");
    }

    public Map<String, Object> getGame(String gameId) {
        String url = String.format("https://store.steampowered.com/api/appdetails?appids=%s&format=json&key=%s", gameId, steamApiKey);

        RestTemplate restTemplate = new RestTemplate();
        Map<java.lang.String, java.lang.Object> response = restTemplate.getForObject(url, Map.class);
        response = (Map<String, Object>) response.get(gameId);
        response = (Map<String, Object>) response.get("data");
        return response;
    }

}
