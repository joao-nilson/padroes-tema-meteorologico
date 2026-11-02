package weather.provider;

import weather.model.WeatherData;

/**
 * Payload format: city|temperature|pressure|wind  (e.g. Juiz de Fora|24.0|1012.0|4.0)
 */
public class WeatherApiProvider implements WeatherProvider {

    public String getName() {
        return "WeatherAPI";
    }

    // Simulates the HTTP call
    public String fetch(String city) {
        return city + "|24.0|1012.0|4.0";
    }

    public WeatherData parse(String raw) {
        try {
            String[] fields = raw.split("\\|");
            return new WeatherData(
                    fields[0],
                    Double.parseDouble(fields[1]),
                    Double.parseDouble(fields[2]),
                    Double.parseDouble(fields[3]));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid WeatherAPI payload", e);
        }
    }
}
