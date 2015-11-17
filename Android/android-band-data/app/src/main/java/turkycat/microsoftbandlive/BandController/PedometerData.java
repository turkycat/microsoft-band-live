package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandPedometerEvent;

/**
 * an immutable class to store current pedometer data
 */
public class PedometerData
{
    private final long totalSteps;

    public PedometerData()
    {
        this( 0 );
    }

    public PedometerData( long totalSteps )
    {
        this.totalSteps = totalSteps;
    }

    public PedometerData( BandPedometerEvent event )
    {
        this.totalSteps = event.getTotalSteps();
    }

    public long getTotalSteps()
    {
        return totalSteps;
    }
}
