package weather.provider;

import weather.model.WeatherData;

import java.util.HashMap;
import java.util.Map;

/**
 * Payload format: name=Juiz de Fora;temp=25.0;pressure=1013.0;wind=5.0
 */
public class OpenWeatherProvider implements WeatherProvider {

    public String getName() {
        return "OpenWeather";
    }

    // Simulates the HTTP call
    public String fetch(String city) {
        return "name=" + city + ";temp=25.0;pressure=1013.0;wind=5.0";
    }

    public WeatherData parse(String raw) {
        try {
            Map<String, String> fields = new HashMap<>();
            for (String pair : raw.split(";")) {
                String[] keyValue = pair.split("=");
                fields.put(keyValue[0], keyValue[1]);
            }
            return new WeatherData(
                    fields.get("name"),
                    Double.parseDouble(fields.get("temp")),
                    Double.parseDouble(fields.get("pressure")),
                    Double.parseDouble(fields.get("wind")));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid OpenWeather payload", e);
        }
    }
}
