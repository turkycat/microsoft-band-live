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
    private BarometerData barometerData;

    //***************************************************************
    // constructors
    //***************************************************************/

    public BandSensorData()
    {
        accelerometerData = new AccelerometerData( 0.0, 0.0, 0.0 );
        altimeterData = new AltimeterData( 0, 0, 0, 0, 0, 0, 0f, 0, 0 );
        ambientLightData = new AmbientLightData( 0 );
        barometerData = new BarometerData( 0.0, 0.0 );
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

    public synchronized BarometerData getBarometerData()
    {
        return barometerData;
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

    public void setBarometerData( BarometerData barometerData )
    {
        if( barometerData != null )
        {
            synchronized( this )
            {
                this.barometerData = barometerData;
            }
        }
    }
}
