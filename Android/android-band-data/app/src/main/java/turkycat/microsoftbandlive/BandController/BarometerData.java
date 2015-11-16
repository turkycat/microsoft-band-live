package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandBarometerEvent;

/**
 * an immutable class to store current data from the barometer
 */
public class BarometerData
{
    private static final double KELVIN_OFFSET = 273.15;

    private final double temperature;
    private final double airPressure;

    public BarometerData( double temperature, double airPressure )
    {
        this.temperature = temperature;
        this.airPressure = airPressure;
    }

    public BarometerData( BandBarometerEvent event )
    {
        this.temperature = event.getTemperature();
        this.airPressure = event.getAirPressure();
    }

    public double getAirPressure()
    {
        return airPressure;
    }

    public double getTemperatureC()
    {
        return temperature;
    }

    public double getTemperatureF()
    {
        return convertCelsiusToFahrenheit( temperature );
    }

    public double getTemperatureK()
    {
        return temperature + KELVIN_OFFSET;
    }

    private double convertCelsiusToFahrenheit( double celsius )
    {
        return ( ( celsius * 9.0 ) / 5.0 ) + 32.0;
    }
}
