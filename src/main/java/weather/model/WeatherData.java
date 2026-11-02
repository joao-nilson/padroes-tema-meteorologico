package weather.model;

public class WeatherData {

    private final String city;
    private final double temperature;
    private final double pressure;
    private final double windSpeed;
    private final String temperatureUnit;
    private final String pressureUnit;
    private final String windSpeedUnit;

    // Base units used internally: °C, hPa, m/s
    public WeatherData(String city, double temperature, double pressure, double windSpeed) {
        this(city, temperature, pressure, windSpeed, "°C", "hPa", "m/s");
    }

    public WeatherData(String city, double temperature, double pressure, double windSpeed,
                       String temperatureUnit, String pressureUnit, String windSpeedUnit) {
        this.city = city;
        this.temperature = temperature;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.temperatureUnit = temperatureUnit;
        this.pressureUnit = pressureUnit;
        this.windSpeedUnit = windSpeedUnit;
    }

    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public String getPressureUnit() {
        return pressureUnit;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    @Override
    public String toString() {
        return city + "{temperature=" + temperature + " " + temperatureUnit +
                ", pressure=" + pressure + " " + pressureUnit +
                ", windSpeed=" + windSpeed + " " + windSpeedUnit + "}";
    }
}
