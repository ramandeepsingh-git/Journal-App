package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    private static final String apiKey = "630f386820a59d895fc9f961db162b92";
    private static final String API = "https://api.weatherstack.com/current?access_key=KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWhether(String city){
        String finalAPI = API.replace("CITY",city).replace("KEY",apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body;
    }

}
