package weather.provider;

import java.util.Locale;

/**
 * Factory Method: creates the right provider adapter from its name.
 */
public class WeatherProviderFactory {

    public static WeatherProvider create(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Unknown provider");
        }
        switch (name.trim().toLowerCase(Locale.ROOT)) {
            case "openweather":
                return new OpenWeatherProvider();
            case "weatherapi":
                return new WeatherApiProvider();
            case "custom":
                return new CustomWeatherProvider();
            default:
                throw new IllegalArgumentException("Unknown provider");
        }
    }
}
