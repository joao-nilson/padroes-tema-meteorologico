package weather.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import weather.cache.WeatherCache;
import weather.model.WeatherData;
import weather.provider.WeatherProviderFactory;
import weather.unit.*;

import static org.junit.jupiter.api.Assertions.*;

class WeatherProcessorTest {

    private final WeatherUnitConverter metric = new WeatherUnitConverter(
            new CelsiusStrategy(), new HpaStrategy(), new MetersPerSecondStrategy());

    @BeforeEach
    void clearCache() {
        WeatherCache.getInstance().clear();
    }

    private WeatherProcessor processorFor(String provider, WeatherUnitConverter converter) {
        return new WeatherProcessor(WeatherProviderFactory.create(provider), converter);
    }

    @Test
    void shouldProcessWithOpenWeather() {
        WeatherData data = processorFor("OpenWeather", metric).process("Juiz de Fora");
        assertEquals("Juiz de Fora", data.getCity());
        assertEquals(25.0, data.getTemperature());
        assertEquals(1013.0, data.getPressure());
        assertEquals(5.0, data.getWindSpeed());
    }

    @Test
    void shouldProcessWithWeatherApi() {
        WeatherData data = processorFor("WeatherAPI", metric).process("Juiz de Fora");
        assertEquals(24.0, data.getTemperature());
        assertEquals(1012.0, data.getPressure());
        assertEquals(4.0, data.getWindSpeed());
    }

    @Test
    void shouldProcessWithCustomSource() {
        WeatherData data = processorFor("Custom", metric).process("Juiz de Fora");
        assertEquals(23.0, data.getTemperature());
        assertEquals(1011.0, data.getPressure());
        assertEquals(3.0, data.getWindSpeed());
    }

    @Test
    void shouldConvertUnitsForAnyProvider() {
        WeatherUnitConverter converter = new WeatherUnitConverter(
                new FahrenheitStrategy(), new InHgStrategy(), new KilometersPerHourStrategy());
        WeatherData data = processorFor("OpenWeather", converter).process("Juiz de Fora");
        assertEquals(77.0, data.getTemperature(), 0.001);
        assertEquals(29.91, data.getPressure(), 0.01);
        assertEquals(18.0, data.getWindSpeed(), 0.001);
        assertEquals("°F", data.getTemperatureUnit());
        assertEquals("inHg", data.getPressureUnit());
        assertEquals("km/h", data.getWindSpeedUnit());
    }

    @Test
    void shouldCacheProcessedData() {
        WeatherProcessor processor = processorFor("OpenWeather", metric);
        WeatherData first = processor.process("Juiz de Fora");
        WeatherData second = processor.process("Juiz de Fora");
        assertSame(first, second);
        assertEquals(1, WeatherCache.getInstance().size());
    }

    @Test
    void shouldThrowForUnknownProvider() {
        assertThrows(IllegalArgumentException.class, () -> processorFor("AccuWeather", metric));
    }
}
