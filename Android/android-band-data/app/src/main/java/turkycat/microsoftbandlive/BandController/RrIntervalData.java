package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandRRIntervalEvent;

/**
 * an immutable class to store current rr interval data
 */
public class RrIntervalData
{
    private final double interval;

    public RrIntervalData()
    {
        this( 0.0 );
    }

    public RrIntervalData( double interval )
    {
        this.interval = interval;
    }

    public RrIntervalData( BandRRIntervalEvent event )
    {
        this.interval = event.getInterval();
    }

    public double getInterval()
    {
        return interval;
    }
}
