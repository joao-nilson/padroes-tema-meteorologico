package weather.provider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherProviderFactoryTest {

    @Test
    void shouldCreateOpenWeatherProvider() {
        WeatherProvider provider = WeatherProviderFactory.create("OpenWeather");
        assertTrue(provider instanceof OpenWeatherProvider);
        assertEquals("OpenWeather", provider.getName());
    }

    @Test
    void shouldCreateWeatherApiProvider() {
        WeatherProvider provider = WeatherProviderFactory.create("WeatherAPI");
        assertTrue(provider instanceof WeatherApiProvider);
        assertEquals("WeatherAPI", provider.getName());
    }

    @Test
    void shouldCreateCustomProvider() {
        WeatherProvider provider = WeatherProviderFactory.create("Custom");
        assertTrue(provider instanceof CustomWeatherProvider);
        assertEquals("Custom", provider.getName());
    }

    @Test
    void shouldIgnoreCaseAndSurroundingSpaces() {
        assertTrue(WeatherProviderFactory.create("  WEATHERAPI ") instanceof WeatherApiProvider);
    }

    @Test
    void shouldThrowForUnknownProvider() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> WeatherProviderFactory.create("AccuWeather"));
        assertEquals("Unknown provider", e.getMessage());
    }

    @Test
    void shouldThrowForNullProvider() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> WeatherProviderFactory.create(null));
        assertEquals("Unknown provider", e.getMessage());
    }
}
