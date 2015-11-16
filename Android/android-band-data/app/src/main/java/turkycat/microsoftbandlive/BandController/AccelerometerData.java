package turkycat.microsoftbandlive.BandController;


import com.microsoft.band.sensors.BandAccelerometerEvent;

/**
 * an immutable class to store current data from the accelerometer
 */
public class AccelerometerData
{
    private final double x;
    private final double y;
    private final double z;

    public AccelerometerData( double x, double y, double z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AccelerometerData( BandAccelerometerEvent event )
    {
        if( event == null ) throw new IllegalArgumentException();
        this.x = event.getAccelerationX();
        this.y = event.getAccelerationY();
        this.z = event.getAccelerationZ();
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }
}
