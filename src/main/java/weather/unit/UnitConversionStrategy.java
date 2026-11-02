package weather.unit;

/**
 * Strategy: converts a value from its base unit (°C, hPa or m/s) to another unit.
 */
public interface UnitConversionStrategy {

    double convert(double value);

    String getUnit();
}
