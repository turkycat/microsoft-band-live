package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandGyroscopeEvent;

/**
 * an immutable class to store current data from the gyroscope
 */
public class GyroscopeData
{
    private final float accelerationX;
    private final float accelerationY;
    private final float accelerationZ;
    private final float angularVelocityX;
    private final float angularVelocityY;
    private final float angularVelocityZ;

    public GyroscopeData()
    {
        this( 0f, 0f, 0f, 0f, 0f, 0f );
    }

    public GyroscopeData( float accelerationX, float accelerationY, float accelerationZ, float angularVelocityX, float angularVelocityY, float angularVelocityZ )
    {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.accelerationZ = accelerationZ;
        this.angularVelocityX = angularVelocityX;
        this.angularVelocityY = angularVelocityY;
        this.angularVelocityZ = angularVelocityZ;
    }

    public GyroscopeData( BandGyroscopeEvent event )
    {
        this.accelerationX = event.getAccelerationX();
        this.accelerationY = event.getAccelerationY();
        this.accelerationZ = event.getAccelerationZ();
        this.angularVelocityX = event.getAngularVelocityX();
        this.angularVelocityY = event.getAngularVelocityY();
        this.angularVelocityZ = event.getAngularVelocityZ();
    }

    public float getAccelerationX()
    {
        return accelerationX;
    }

    public float getAccelerationY()
    {
        return accelerationY;
    }

    public float getAccelerationZ()
    {
        return accelerationZ;
    }

    public float getAngularVelocityX()
    {
        return angularVelocityX;
    }

    public float getAngularVelocityY()
    {
        return angularVelocityY;
    }

    public float getAngularVelocityZ()
    {
        return angularVelocityZ;
    }
}
