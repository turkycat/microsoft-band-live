package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.MotionType;

/**
 * an immutable class to store current distance data
 */
public class DistanceData
{
    private final MotionType motionType;
    private final float pace;
    private final float speed;
    private final long totalDistance;

    public DistanceData( MotionType motionType, float pace, float speed, long totalDistance )
    {
        this.motionType = motionType;
        this.pace = pace;
        this.speed = speed;
        this.totalDistance = totalDistance;

    }

    public DistanceData( BandDistanceEvent event )
    {
        this.motionType = event.getMotionType();
        this.pace = event.getPace();
        this.speed = event.getSpeed();
        this.totalDistance = event.getTotalDistance();
    }

    public MotionType getMotionType()
    {
        return motionType;
    }

    public float getPace()
    {
        return pace;
    }

    public float getSpeed()
    {
        return speed;
    }

    public long getTotalDistance()
    {
        return totalDistance;
    }
}
