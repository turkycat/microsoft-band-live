package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandSkinTemperatureEvent;

/**
 * an immutable class to store current skin temperature data
 */
public class SkinTemperatureData
{
    private static final float KELVIN_OFFSET = 273.15f;

    private final float temperature;

    public SkinTemperatureData()
    {
        this( 0f );
    }

    public SkinTemperatureData( float temperature )
    {
        this.temperature = temperature;
    }

    public SkinTemperatureData( BandSkinTemperatureEvent event )
    {
        this.temperature = event.getTemperature();
    }

    public float getTemperatureC()
    {
        return temperature;
    }

    public float getTemperatureF()
    {
        return convertCelsiusToFahrenheit( temperature );
    }

    public float getTemperatureK()
    {
        return temperature + KELVIN_OFFSET;
    }

    private float convertCelsiusToFahrenheit( float celsius )
    {
        return (float)( ( ( celsius * 9.0 ) / 5.0 ) + 32.0 );
    }
}
