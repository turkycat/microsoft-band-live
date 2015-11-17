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
    private GsrData gsrData;
    private GyroscopeData gyroscopeData;
    private HeartRateData heartRateData;
    private PedometerData pedometerData;
    private RrIntervalData rrIntervalData;

    //***************************************************************
    // constructors
    //***************************************************************/

    public BandSensorData()
    {
        this.accelerometerData = new AccelerometerData();
        this.altimeterData = new AltimeterData();
        this.ambientLightData = new AmbientLightData();
        this.barometerData = new BarometerData();
        this.calorieData = new CalorieData();
        this.contactData = new ContactData();
        this.distanceData = new DistanceData();
        this.gsrData = new GsrData();
        this.gyroscopeData = new GyroscopeData();
        this.heartRateData = new HeartRateData();
        this.pedometerData = new PedometerData();
        this.rrIntervalData = new RrIntervalData();
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

    public synchronized GsrData getGsrData()
    {
        return gsrData;
    }

    public synchronized GyroscopeData getGyroscopeData()
    {
        return gyroscopeData;
    }

    public synchronized HeartRateData getHeartRateData()
    {
        return heartRateData;
    }

    public synchronized PedometerData getPedometerData()
    {
        return pedometerData;
    }

    public synchronized RrIntervalData getRrIntervalData()
    {
        return rrIntervalData;
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

    public void setGsrData( GsrData gsrData )
    {
        if( gsrData != null )
        {
            synchronized( this )
            {
                this.gsrData = gsrData;
            }
        }
    }

    public void setGyroscopeData( GyroscopeData gyroscopeData )
    {
        if( gyroscopeData != null )
        {
            synchronized( this )
            {
                this.gyroscopeData = gyroscopeData;
            }
        }
    }

    public void setHeartRateData( HeartRateData heartRateData )
    {
        if( heartRateData != null )
        {
            synchronized( this )
            {
                this.heartRateData = heartRateData;
            }
        }
    }

    public void setPedometerData( PedometerData pedometerData )
    {
        if( pedometerData != null )
        {
            synchronized( this )
            {
                this.pedometerData = pedometerData;
            }
        }
    }

    public void setRrIntervalData( RrIntervalData rrIntervalData )
    {
        if( rrIntervalData != null )
        {
            synchronized( this )
            {
                this.rrIntervalData = rrIntervalData;
            }
        }
    }
}
