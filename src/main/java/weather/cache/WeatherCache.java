package weather.cache;

import weather.model.WeatherData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton: single in-memory store shared by the whole application,
 * used to avoid fetching the same weather data twice.
 */
public class WeatherCache {

    private WeatherCache() {}
    private static final WeatherCache instance = new WeatherCache();
    public static WeatherCache getInstance() {
        return instance;
    }

    private final Map<String, WeatherData> data = new ConcurrentHashMap<>();

    public void put(String key, WeatherData weatherData) {
        data.put(key, weatherData);
    }

    public WeatherData get(String key) {
        return data.get(key);
    }

    public boolean contains(String key) {
        return data.containsKey(key);
    }

    public int size() {
        return data.size();
    }

    public void clear() {
        data.clear();
    }
}
