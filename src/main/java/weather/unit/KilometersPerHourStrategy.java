package weather.unit;

public class KilometersPerHourStrategy implements UnitConversionStrategy {

    public double convert(double value) {
        return value * 3.6;
    }

    public String getUnit() {
        return "km/h";
    }
}
