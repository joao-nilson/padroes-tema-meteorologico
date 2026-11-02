package weather.provider;

import weather.model.WeatherData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom source (e.g. a local weather station).
 * Payload format: STATION[Juiz de Fora] T=23.0C P=1011.0hPa W=3.0m/s
 */
public class CustomWeatherProvider implements WeatherProvider {

    private static final Pattern FORMAT = Pattern.compile(
            "STATION\\[(.+)\\] T=(-?\\d+(?:\\.\\d+)?)C P=(\\d+(?:\\.\\d+)?)hPa W=(\\d+(?:\\.\\d+)?)m/s");

    public String getName() {
        return "Custom";
    }

    // Simulates reading from the station
    public String fetch(String city) {
        return "STATION[" + city + "] T=23.0C P=1011.0hPa W=3.0m/s";
    }

    public WeatherData parse(String raw) {
        Matcher matcher = FORMAT.matcher(raw == null ? "" : raw);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid Custom payload");
        }
        return new WeatherData(
                matcher.group(1),
                Double.parseDouble(matcher.group(2)),
                Double.parseDouble(matcher.group(3)),
                Double.parseDouble(matcher.group(4)));
    }
}
