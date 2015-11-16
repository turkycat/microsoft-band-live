package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandGsrEvent;

/**
 * an immutable class to store current data from the galvanic skin response sensor
 */
public class GsrData
{
    private final int resistance;

    public GsrData()
    {
        this( 0 );
    }

    public GsrData( int resistance )
    {
        this.resistance = resistance;
    }

    public GsrData( BandGsrEvent event )
    {
        this.resistance = event.getResistance();
    }

    public int getResistance()
    {
        return resistance;
    }
}
