package weather.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import weather.model.WeatherData;

import static org.junit.jupiter.api.Assertions.*;

class WeatherCacheTest {

    @BeforeEach
    void clearCache() {
        WeatherCache.getInstance().clear();
    }

    @Test
    void shouldAlwaysReturnTheSameInstance() {
        assertSame(WeatherCache.getInstance(), WeatherCache.getInstance());
    }

    @Test
    void shouldShareDataBetweenCallsToGetInstance() {
        WeatherData data = new WeatherData("Juiz de Fora", 25.0, 1013.0, 5.0);
        WeatherCache.getInstance().put("jf", data);
        assertSame(data, WeatherCache.getInstance().get("jf"));
    }

    @Test
    void shouldReturnNullForUnknownKey() {
        assertNull(WeatherCache.getInstance().get("unknown"));
        assertFalse(WeatherCache.getInstance().contains("unknown"));
    }

    @Test
    void shouldClearAllData() {
        WeatherCache.getInstance().put("jf", new WeatherData("Juiz de Fora", 25.0, 1013.0, 5.0));
        assertEquals(1, WeatherCache.getInstance().size());
        WeatherCache.getInstance().clear();
        assertEquals(0, WeatherCache.getInstance().size());
    }
}
