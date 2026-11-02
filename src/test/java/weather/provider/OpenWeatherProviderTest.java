package weather.provider;

import org.junit.jupiter.api.Test;
import weather.model.WeatherData;

import static org.junit.jupiter.api.Assertions.*;

class OpenWeatherProviderTest {

    private final WeatherProvider provider = new OpenWeatherProvider();

    @Test
    void shouldParseOwnPayload() {
        WeatherData data = provider.parse(provider.fetch("Juiz de Fora"));
        assertEquals("Juiz de Fora", data.getCity());
        assertEquals(25.0, data.getTemperature());
        assertEquals(1013.0, data.getPressure());
        assertEquals(5.0, data.getWindSpeed());
    }

    @Test
    void shouldThrowForMissingField() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> provider.parse("name=Rio;temp=25.0"));
        assertEquals("Invalid OpenWeather payload", e.getMessage());
    }

    @Test
    void shouldThrowForNonNumericValue() {
        assertThrows(IllegalArgumentException.class,
                () -> provider.parse("name=Rio;temp=hot;pressure=1013.0;wind=5.0"));
    }
}
