package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.HeartRateQuality;

/**
 * an immutable class to store current data from the heart rate sensor
 */
public class HeartRateData
{
    private final HeartRateQuality quality;
    private final int heartRate;

    public HeartRateData()
    {
        this( HeartRateQuality.ACQUIRING, 0 );
    }

    public HeartRateData( HeartRateQuality quality, int heartRate )
    {
        this.quality = quality;
        this.heartRate = heartRate;
    }

    public HeartRateData( BandHeartRateEvent event )
    {
        this.quality = event.getQuality();
        this.heartRate = event.getHeartRate();
    }

    public HeartRateQuality getQuality()
    {
        return quality;
    }

    public int getHeartRate()
    {
        return heartRate;
    }
}
