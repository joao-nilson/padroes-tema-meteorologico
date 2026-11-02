package weather.provider;

import weather.model.WeatherData;

/**
 * Adapter interface: hides each weather API's payload format behind one contract.
 */
public interface WeatherProvider {

    String getName();

    // Returns the raw payload in the API's own format
    String fetch(String city);

    // Adapts the raw payload to WeatherData (base units: °C, hPa, m/s)
    WeatherData parse(String raw);
}
