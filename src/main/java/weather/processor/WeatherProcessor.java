package weather.processor;

import weather.model.WeatherData;
import weather.provider.WeatherProvider;
import weather.unit.WeatherUnitConverter;

/**
 * Concrete processor: fetch and parse are delegated to a provider (usually
 * created by WeatherProviderFactory).
 */
public class WeatherProcessor extends BaseWeatherProcessor {

    private final WeatherProvider provider;

    public WeatherProcessor(WeatherProvider provider, WeatherUnitConverter converter) {
        super(converter);
        this.provider = provider;
    }

    @Override
    protected String fetch(String city) {
        return provider.fetch(city);
    }

    @Override
    protected WeatherData parse(String raw) {
        return provider.parse(raw);
    }
}
