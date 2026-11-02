package weather.unit;

public class FahrenheitStrategy implements UnitConversionStrategy {

    public double convert(double value) {
        return value * 9 / 5 + 32;
    }

    public String getUnit() {
        return "°F";
    }
}
