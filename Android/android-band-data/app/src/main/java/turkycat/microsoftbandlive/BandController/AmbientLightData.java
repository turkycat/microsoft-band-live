package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandAmbientLightEvent;

/**
 * an immutable class to store current data from the ambient light sensor
 */
public class AmbientLightData
{
    private final int brightness;

    public AmbientLightData()
    {
        this( 0 );
    }

    public AmbientLightData( int brightness )
    {
        this.brightness = brightness;
    }

    public AmbientLightData( BandAmbientLightEvent event )
    {
        if( event == null ) throw new IllegalArgumentException();
        this.brightness = event.getBrightness();
    }

    public int getBrightness()
    {
        return brightness;
    }
}
