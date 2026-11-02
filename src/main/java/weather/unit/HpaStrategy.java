package weather.unit;

public class HpaStrategy implements UnitConversionStrategy {

    public double convert(double value) {
        return value;
    }

    public String getUnit() {
        return "hPa";
    }
}
