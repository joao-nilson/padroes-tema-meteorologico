package weather.provider;

import org.junit.jupiter.api.Test;
import weather.model.WeatherData;

import static org.junit.jupiter.api.Assertions.*;

class CustomWeatherProviderTest {

    private final WeatherProvider provider = new CustomWeatherProvider();

    @Test
    void shouldParseOwnPayload() {
        WeatherData data = provider.parse(provider.fetch("Juiz de Fora"));
        assertEquals("Juiz de Fora", data.getCity());
        assertEquals(23.0, data.getTemperature());
        assertEquals(1011.0, data.getPressure());
        assertEquals(3.0, data.getWindSpeed());
    }

    @Test
    void shouldParseNegativeTemperature() {
        WeatherData data = provider.parse("STATION[Ushuaia] T=-5.5C P=990hPa W=12m/s");
        assertEquals(-5.5, data.getTemperature());
    }

    @Test
    void shouldThrowForInvalidPayload() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> provider.parse("garbage"));
        assertEquals("Invalid Custom payload", e.getMessage());
    }

    @Test
    void shouldThrowForNullPayload() {
        assertThrows(IllegalArgumentException.class, () -> provider.parse(null));
    }
}
