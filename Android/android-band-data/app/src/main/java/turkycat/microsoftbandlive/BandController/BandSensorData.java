package turkycat.microsoftbandlive.BandController;

/**
 * A thread-safe class to manage Microsoft Band sensor data
 * Created by turkycat on 11/15/2015.
 */
public class BandSensorData
{

    //current sensor data
    private AccelerometerData accelerometerData;
    private AltimeterData altimeterData;
    private AmbientLightData ambientLightData;

    //***************************************************************
    // constructors
    //***************************************************************/

    public BandSensorData()
    {
        accelerometerData = new AccelerometerData( 0.0, 0.0, 0.0 );
        altimeterData = new AltimeterData( 0, 0, 0, 0, 0, 0, 0, 0, 0 );
        ambientLightData = new AmbientLightData( 0 );
    }

    //***************************************************************
    // public getters
    //***************************************************************/

    public synchronized AccelerometerData getAccelerometerData()
    {
        return accelerometerData;
    }

    public synchronized AltimeterData getAltimeterData()
    {
        return altimeterData;
    }

    public synchronized AmbientLightData getAmbientLightData()
    {
        return ambientLightData;
    }

    //***************************************************************
    // public setters
    //***************************************************************/

    public void setAccelerometerData( AccelerometerData accelerometerData )
    {
        if( ambientLightData != null )
        {
            synchronized( this )
            {
                this.accelerometerData = accelerometerData;
            }
        }
    }

    public void setAltimeterData( AltimeterData altimeterData )
    {
        if( ambientLightData != null )
        {
            synchronized( this )
            {
                this.altimeterData = altimeterData;
            }
        }
    }

    public void setAmbientLightData( AmbientLightData ambientLightData )
    {
        if( ambientLightData != null )
        {
            synchronized( this )
            {
                this.ambientLightData = ambientLightData;
            }
        }
    }
}
