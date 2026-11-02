package weather.processor;

import weather.cache.WeatherCache;
import weather.model.WeatherData;
import weather.unit.WeatherUnitConverter;

import java.util.Locale;

/**
 * Template Method: process() fixes the order
 * fetch -> parse -> validate -> unit-convert -> cache.
 * Subclasses supply fetch and parse; validate, convert and cache have default
 * implementations that can be overridden.
 */
public abstract class BaseWeatherProcessor {

    private final WeatherUnitConverter converter;

    protected BaseWeatherProcessor(WeatherUnitConverter converter) {
        this.converter = converter;
    }

    // The template method
    public final WeatherData process(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }

        String key = cacheKey(city);
        WeatherData cached = WeatherCache.getInstance().get(key);
        if (cached != null) {
            return cached;
        }

        String raw = fetch(city);
        WeatherData data = parse(raw);
        validate(data);
        WeatherData converted = convert(data);
        cache(key, converted);
        return converted;
    }

    protected abstract String fetch(String city);

    protected abstract WeatherData parse(String raw);

    // Validates in base units (°C, hPa, m/s), before conversion
    protected void validate(WeatherData data) {
        if (outOfRange(data.getTemperature(), -90, 60)) {
            throw new IllegalArgumentException("Invalid temperature");
        }
        if (outOfRange(data.getPressure(), 870, 1085)) {
            throw new IllegalArgumentException("Invalid pressure");
        }
        if (outOfRange(data.getWindSpeed(), 0, 120)) {
            throw new IllegalArgumentException("Invalid wind speed");
        }
    }

    protected WeatherData convert(WeatherData data) {
        return converter.convert(data);
    }

    protected void cache(String key, WeatherData data) {
        WeatherCache.getInstance().put(key, data);
    }

    // Same city in different units must not share a cache entry
    private String cacheKey(String city) {
        return city.trim().toLowerCase(Locale.ROOT) + "|" + converter.getUnits();
    }

    // Written as !(in range) so NaN is rejected too
    private static boolean outOfRange(double value, double min, double max) {
        return !(value >= min && value <= max);
    }
}
