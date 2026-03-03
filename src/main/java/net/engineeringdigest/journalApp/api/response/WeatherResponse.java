package net.engineeringdigest.journalApp.api.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;// version 2.11.1
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {

    public Current current;
    @Getter
    @Setter
    public class Current{
        public int temperature;
        @JsonProperty("weather_descriptions")
        public List<String> weatherDescriptions;
        @JsonProperty("wind_speed")
        public int windSpeed;
        public int humidity;
        public int feelslike;
        public int visibility;
    }


}
