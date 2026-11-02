package weather.unit;

import weather.model.WeatherData;

/**
 * Strategy context: applies one strategy to each measurement.
 * Expects data in base units (°C, hPa, m/s).
 */
public class WeatherUnitConverter {

    private final UnitConversionStrategy temperature;
    private final UnitConversionStrategy pressure;
    private final UnitConversionStrategy windSpeed;

    public WeatherUnitConverter(UnitConversionStrategy temperature,
                                UnitConversionStrategy pressure,
                                UnitConversionStrategy windSpeed) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
    }

    public WeatherData convert(WeatherData data) {
        return new WeatherData(
                data.getCity(),
                temperature.convert(data.getTemperature()),
                pressure.convert(data.getPressure()),
                windSpeed.convert(data.getWindSpeed()),
                temperature.getUnit(),
                pressure.getUnit(),
                windSpeed.getUnit());
    }

    public String getUnits() {
        return temperature.getUnit() + "," + pressure.getUnit() + "," + windSpeed.getUnit();
    }
}
