package weather.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import weather.cache.WeatherCache;
import weather.model.WeatherData;
import weather.unit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseWeatherProcessorTest {

    private final WeatherUnitConverter metric = new WeatherUnitConverter(
            new CelsiusStrategy(), new HpaStrategy(), new MetersPerSecondStrategy());

    private final WeatherUnitConverter other = new WeatherUnitConverter(
            new FahrenheitStrategy(), new InHgStrategy(), new KilometersPerHourStrategy());

    @BeforeEach
    void clearCache() {
        WeatherCache.getInstance().clear();
    }

    // Processor that always returns the given data and counts fetches
    private static class FakeProcessor extends BaseWeatherProcessor {
        private final WeatherData data;
        int fetchCount = 0;

        FakeProcessor(WeatherUnitConverter converter, WeatherData data) {
            super(converter);
            this.data = data;
        }

        @Override
        protected String fetch(String city) {
            fetchCount++;
            return "raw";
        }

        @Override
        protected WeatherData parse(String raw) {
            return data;
        }
    }

    private WeatherData data(double temperature, double pressure, double windSpeed) {
        return new WeatherData("Juiz de Fora", temperature, pressure, windSpeed);
    }

    @Test
    void shouldExecuteStepsInOrder() {
        List<String> steps = new ArrayList<>();
        BaseWeatherProcessor processor = new BaseWeatherProcessor(metric) {
            @Override
            protected String fetch(String city) {
                steps.add("fetch");
                return "raw";
            }

            @Override
            protected WeatherData parse(String raw) {
                steps.add("parse");
                return data(25.0, 1013.0, 5.0);
            }

            @Override
            protected void validate(WeatherData data) {
                steps.add("validate");
                super.validate(data);
            }

            @Override
            protected WeatherData convert(WeatherData data) {
                steps.add("convert");
                return super.convert(data);
            }

            @Override
            protected void cache(String key, WeatherData data) {
                steps.add("cache");
                super.cache(key, data);
            }
        };

        processor.process("Juiz de Fora");

        assertEquals(Arrays.asList("fetch", "parse", "validate", "convert", "cache"), steps);
    }

    @Test
    void shouldConvertUnitsUsingStrategy() {
        FakeProcessor processor = new FakeProcessor(other, data(25.0, 1013.0, 5.0));
        WeatherData result = processor.process("Juiz de Fora");
        assertEquals(77.0, result.getTemperature(), 0.001);
        assertEquals(29.91, result.getPressure(), 0.01);
        assertEquals(18.0, result.getWindSpeed(), 0.001);
    }

    @Test
    void shouldStoreResultInCache() {
        FakeProcessor processor = new FakeProcessor(metric, data(25.0, 1013.0, 5.0));
        WeatherData result = processor.process("Juiz de Fora");
        assertEquals(1, WeatherCache.getInstance().size());
        assertTrue(WeatherCache.getInstance().contains("juiz de fora|°C,hPa,m/s"));
        assertSame(result, WeatherCache.getInstance().get("juiz de fora|°C,hPa,m/s"));
    }

    @Test
    void shouldNotFetchAgainWhenDataIsCached() {
        FakeProcessor processor = new FakeProcessor(metric, data(25.0, 1013.0, 5.0));
        WeatherData first = processor.process("Juiz de Fora");
        WeatherData second = processor.process("juiz de fora ");
        assertEquals(1, processor.fetchCount);
        assertSame(first, second);
    }

    @Test
    void shouldShareCacheBetweenProcessors() {
        FakeProcessor first = new FakeProcessor(metric, data(25.0, 1013.0, 5.0));
        FakeProcessor second = new FakeProcessor(metric, data(30.0, 1000.0, 2.0));
        first.process("Juiz de Fora");
        WeatherData result = second.process("Juiz de Fora");
        assertEquals(0, second.fetchCount);
        assertEquals(25.0, result.getTemperature());
    }

    @Test
    void shouldCacheSameCityInDifferentUnitsSeparately() {
        FakeProcessor metricProcessor = new FakeProcessor(metric, data(25.0, 1013.0, 5.0));
        FakeProcessor otherProcessor = new FakeProcessor(other, data(25.0, 1013.0, 5.0));
        WeatherData inCelsius = metricProcessor.process("Juiz de Fora");
        WeatherData inFahrenheit = otherProcessor.process("Juiz de Fora");
        assertEquals(25.0, inCelsius.getTemperature(), 0.001);
        assertEquals(77.0, inFahrenheit.getTemperature(), 0.001);
        assertEquals(1, otherProcessor.fetchCount);
        assertEquals(2, WeatherCache.getInstance().size());
    }

    @Test
    void shouldRejectBlankCity() {
        FakeProcessor processor = new FakeProcessor(metric, data(25.0, 1013.0, 5.0));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("  "));
        assertEquals("City is required", e.getMessage());
        assertEquals(0, processor.fetchCount);
    }

    @Test
    void shouldRejectNullCity() {
        FakeProcessor processor = new FakeProcessor(metric, data(25.0, 1013.0, 5.0));
        assertThrows(IllegalArgumentException.class, () -> processor.process(null));
    }

    @Test
    void shouldRejectInvalidTemperature() {
        FakeProcessor processor = new FakeProcessor(metric, data(999.0, 1013.0, 5.0));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("Juiz de Fora"));
        assertEquals("Invalid temperature", e.getMessage());
    }

    @Test
    void shouldRejectInvalidPressure() {
        FakeProcessor processor = new FakeProcessor(metric, data(25.0, 10.0, 5.0));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("Juiz de Fora"));
        assertEquals("Invalid pressure", e.getMessage());
    }

    @Test
    void shouldRejectNegativeWindSpeed() {
        FakeProcessor processor = new FakeProcessor(metric, data(25.0, 1013.0, -1.0));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> processor.process("Juiz de Fora"));
        assertEquals("Invalid wind speed", e.getMessage());
    }

    @Test
    void shouldRejectNaN() {
        FakeProcessor processor = new FakeProcessor(metric, data(Double.NaN, 1013.0, 5.0));
        assertThrows(IllegalArgumentException.class, () -> processor.process("Juiz de Fora"));
    }

    @Test
    void shouldAcceptValuesOnTheLimits() {
        FakeProcessor processor = new FakeProcessor(metric, data(-90.0, 870.0, 0.0));
        assertEquals(-90.0, processor.process("Juiz de Fora").getTemperature());
    }

    @Test
    void shouldNotCacheInvalidData() {
        FakeProcessor processor = new FakeProcessor(metric, data(999.0, 1013.0, 5.0));
        assertThrows(IllegalArgumentException.class, () -> processor.process("Juiz de Fora"));
        assertEquals(0, WeatherCache.getInstance().size());
    }
}
