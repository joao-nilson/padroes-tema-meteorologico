package weather.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitConversionStrategyTest {

    @Test
    void shouldKeepCelsius() {
        UnitConversionStrategy strategy = new CelsiusStrategy();
        assertEquals(25.0, strategy.convert(25.0));
        assertEquals("°C", strategy.getUnit());
    }

    @Test
    void shouldConvertCelsiusToFahrenheit() {
        UnitConversionStrategy strategy = new FahrenheitStrategy();
        assertEquals(32.0, strategy.convert(0.0), 0.001);
        assertEquals(77.0, strategy.convert(25.0), 0.001);
        assertEquals(212.0, strategy.convert(100.0), 0.001);
        assertEquals(-40.0, strategy.convert(-40.0), 0.001);
        assertEquals("°F", strategy.getUnit());
    }

    @Test
    void shouldKeepHectopascal() {
        UnitConversionStrategy strategy = new HpaStrategy();
        assertEquals(1013.0, strategy.convert(1013.0));
        assertEquals("hPa", strategy.getUnit());
    }

    @Test
    void shouldConvertHectopascalToInchesOfMercury() {
        UnitConversionStrategy strategy = new InHgStrategy();
        assertEquals(29.91, strategy.convert(1013.0), 0.01);
        assertEquals(0.0, strategy.convert(0.0), 0.001);
        assertEquals("inHg", strategy.getUnit());
    }

    @Test
    void shouldKeepMetersPerSecond() {
        UnitConversionStrategy strategy = new MetersPerSecondStrategy();
        assertEquals(5.0, strategy.convert(5.0));
        assertEquals("m/s", strategy.getUnit());
    }

    @Test
    void shouldConvertMetersPerSecondToKilometersPerHour() {
        UnitConversionStrategy strategy = new KilometersPerHourStrategy();
        assertEquals(18.0, strategy.convert(5.0), 0.001);
        assertEquals(36.0, strategy.convert(10.0), 0.001);
        assertEquals("km/h", strategy.getUnit());
    }
}
