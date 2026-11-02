package weather.unit;

public class CelsiusStrategy implements UnitConversionStrategy {

    public double convert(double value) {
        return value;
    }

    public String getUnit() {
        return "°C";
    }
}
