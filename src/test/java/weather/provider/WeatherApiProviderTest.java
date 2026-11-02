package weather.provider;

import org.junit.jupiter.api.Test;
import weather.model.WeatherData;

import static org.junit.jupiter.api.Assertions.*;

class WeatherApiProviderTest {

    private final WeatherProvider provider = new WeatherApiProvider();

    @Test
    void shouldParseOwnPayload() {
        WeatherData data = provider.parse(provider.fetch("Juiz de Fora"));
        assertEquals("Juiz de Fora", data.getCity());
        assertEquals(24.0, data.getTemperature());
        assertEquals(1012.0, data.getPressure());
        assertEquals(4.0, data.getWindSpeed());
    }

    @Test
    void shouldThrowForMissingField() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> provider.parse("Rio|24.0"));
        assertEquals("Invalid WeatherAPI payload", e.getMessage());
    }

    @Test
    void shouldThrowForNullPayload() {
        assertThrows(IllegalArgumentException.class, () -> provider.parse(null));
    }
}
