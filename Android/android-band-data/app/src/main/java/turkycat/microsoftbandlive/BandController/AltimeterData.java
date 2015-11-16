package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandAltimeterEvent;

/**
 * an immutable class to store current data from the altimeter
 */
public class AltimeterData
{
    private final long totalGain;
    private final long totalLoss;
    private final long steppingGain;
    private final long steppingLoss;
    private final long stepsAscended;
    private final long stepsDescended;
    private final float rate;
    private final long flightsAscended;
    private final long flightsDescended;

    public AltimeterData( long totalGain, long totalLoss, long steppingGain, long steppingLoss, long stepsAscended, long stepsDescended, float rate, long flightsAscended, long flightsDescended )
    {
        this.totalGain = totalGain;
        this.totalLoss = totalLoss;
        this.steppingGain = steppingGain;
        this.steppingLoss = steppingLoss;
        this.stepsAscended = stepsAscended;
        this.stepsDescended = stepsDescended;
        this.rate = rate;
        this.flightsAscended = flightsAscended;
        this.flightsDescended = flightsDescended;
    }

    public AltimeterData( BandAltimeterEvent event )
    {
        this.totalGain = event.getTotalGain();
        this.totalLoss = event.getTotalLoss();
        this.steppingGain = event.getSteppingGain();
        this.steppingLoss = event.getSteppingLoss();
        this.stepsAscended = event.getStepsAscended();
        this.stepsDescended = event.getStepsDescended();
        this.rate = event.getRate();
        this.flightsAscended = event.getFlightsAscended();
        this.flightsDescended = event.getStepsDescended();
    }

    public long getTotalGain()
    {
        return totalGain;
    }

    public long getTotalLoss()
    {
        return totalLoss;
    }

    public long getSteppingGain()
    {
        return steppingGain;
    }

    public long getSteppingLoss()
    {
        return steppingLoss;
    }

    public long getStepsAscended()
    {
        return stepsAscended;
    }

    public long getStepsDescended()
    {
        return stepsDescended;
    }

    public float getRate()
    {
        return rate;
    }

    public long getFlightsAscended()
    {
        return flightsAscended;
    }

    public long getFlightsDescended()
    {
        return flightsDescended;
    }
}
