package weather.unit;

import org.junit.jupiter.api.Test;
import weather.model.WeatherData;

import static org.junit.jupiter.api.Assertions.*;

class WeatherUnitConverterTest {

    private final WeatherData base = new WeatherData("Juiz de Fora", 25.0, 1013.0, 5.0);

    @Test
    void shouldKeepBaseUnits() {
        WeatherUnitConverter converter = new WeatherUnitConverter(
                new CelsiusStrategy(), new HpaStrategy(), new MetersPerSecondStrategy());
        WeatherData result = converter.convert(base);
        assertEquals(25.0, result.getTemperature());
        assertEquals(1013.0, result.getPressure());
        assertEquals(5.0, result.getWindSpeed());
        assertEquals("°C,hPa,m/s", converter.getUnits());
    }

    @Test
    void shouldConvertAllMeasurements() {
        WeatherUnitConverter converter = new WeatherUnitConverter(
                new FahrenheitStrategy(), new InHgStrategy(), new KilometersPerHourStrategy());
        WeatherData result = converter.convert(base);
        assertEquals("Juiz de Fora", result.getCity());
        assertEquals(77.0, result.getTemperature(), 0.001);
        assertEquals(29.91, result.getPressure(), 0.01);
        assertEquals(18.0, result.getWindSpeed(), 0.001);
        assertEquals("°F", result.getTemperatureUnit());
        assertEquals("inHg", result.getPressureUnit());
        assertEquals("km/h", result.getWindSpeedUnit());
        assertEquals("°F,inHg,km/h", converter.getUnits());
    }

    @Test
    void shouldMixStrategiesIndependently() {
        WeatherUnitConverter converter = new WeatherUnitConverter(
                new FahrenheitStrategy(), new HpaStrategy(), new MetersPerSecondStrategy());
        WeatherData result = converter.convert(base);
        assertEquals(77.0, result.getTemperature(), 0.001);
        assertEquals(1013.0, result.getPressure());
        assertEquals(5.0, result.getWindSpeed());
    }

    @Test
    void shouldNotChangeOriginalData() {
        new WeatherUnitConverter(
                new FahrenheitStrategy(), new InHgStrategy(), new KilometersPerHourStrategy()).convert(base);
        assertEquals(25.0, base.getTemperature());
        assertEquals("°C", base.getTemperatureUnit());
    }
}
