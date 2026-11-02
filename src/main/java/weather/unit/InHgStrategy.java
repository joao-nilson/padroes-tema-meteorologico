package weather.unit;

public class InHgStrategy implements UnitConversionStrategy {

    public double convert(double value) {
        return value * 0.02953;
    }

    public String getUnit() {
        return "inHg";
    }
}
