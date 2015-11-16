package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.MotionType;

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
    private CalorieData calorieData;
    private ContactData contactData;
    private DistanceData distanceData;

    //***************************************************************
    // constructors
    //***************************************************************/

    public BandSensorData()
    {
        accelerometerData = new AccelerometerData();
        altimeterData = new AltimeterData();
        ambientLightData = new AmbientLightData();
        barometerData = new BarometerData();
        calorieData = new CalorieData();
        contactData = new ContactData();
        distanceData = new DistanceData();
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

    public synchronized CalorieData getCalorieData()
    {
        return calorieData;
    }

    public synchronized ContactData getContactData()
    {
        return contactData;
    }

    public synchronized DistanceData getDistanceData()
    {
        return distanceData;
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

    public void setCalorieData( CalorieData calorieData )
    {
        if( calorieData != null )
        {
            synchronized( this )
            {
                this.calorieData = calorieData;
            }
        }
    }

    public void setContactData( ContactData contactData )
    {
        if( contactData != null )
        {
            synchronized( this )
            {
                this.contactData = contactData;
            }
        }
    }

    public void setDistanceData( DistanceData distanceData )
    {
        if( distanceData != null )
        {
            synchronized( this )
            {
                this.distanceData = distanceData;
            }
        }
    }
}
