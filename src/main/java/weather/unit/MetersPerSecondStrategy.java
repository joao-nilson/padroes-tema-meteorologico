package weather.unit;

public class MetersPerSecondStrategy implements UnitConversionStrategy {

    public double convert(double value) {
        return value;
    }

    public String getUnit() {
        return "m/s";
    }
}
