package turkycat.microsoftbandlive.BandController;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandAltimeterEvent;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.BandAmbientLightEvent;
import com.microsoft.band.sensors.BandAmbientLightEventListener;
import com.microsoft.band.sensors.BandBarometerEvent;
import com.microsoft.band.sensors.BandBarometerEventListener;
import com.microsoft.band.sensors.BandCaloriesEvent;
import com.microsoft.band.sensors.BandCaloriesEventListener;
import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactEventListener;
import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.BandDistanceEventListener;
import com.microsoft.band.sensors.BandGsrEvent;
import com.microsoft.band.sensors.BandGsrEventListener;
import com.microsoft.band.sensors.BandGyroscopeEvent;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEvent;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.BandSensorManager;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.BandUVEvent;
import com.microsoft.band.sensors.BandUVEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;

/**
 * A thread-safe class to manage Microsoft Band sensor registration and provide access to current sensor data
 * Created by turkycat on 11/15/2015.
 */
public class BandController
{
    //optional event listener for status changes
    private BandStatusEventListener listener;

    //current Band client or null
    private BandClient client;

    //current sensor data
    private final BandSensorData bandSensorData;

    //***************************************************************
    // constructors
    //***************************************************************/

    public BandController()
    {
        this.client = null;
        this.bandSensorData = new BandSensorData();
    }

    //***************************************************************
    // public functions
    //***************************************************************/

    public synchronized void connect( final Activity activity )
    {
        if( isConnected() ) return;
        new BandConnectionTask().execute( activity );
    }

    public synchronized void disconnect()
    {
        if( !isConnected() ) return;

        client.disconnect();
        client = null;

        if( listener != null )
        {
            listener.onBandConnectionStatusChanged( BandStatusEventListener.BandConnectionStatus.NOT_CONNECTED );
        }
    }

    public synchronized boolean isConnected()
    {
        return client != null && client.isConnected();
    }

    public BandSensorData getBandSensorData()
    {
        return bandSensorData;
    }

    public synchronized void setBandStatusEventListener( BandStatusEventListener listener )
    {
        this.listener = listener;
    }

    public synchronized boolean startAllSensors( Activity activity ) throws BandIOException
    {
        if( !isConnected() ) return false;

        BandSensorManager sensorManager = client.getSensorManager();
        sensorManager.registerAccelerometerEventListener( accelerometerEventListener, SampleRate.MS128 );
        sensorManager.registerAltimeterEventListener( altimeterEventListener );
        sensorManager.registerAmbientLightEventListener( ambientLightEventListener );
        sensorManager.registerBarometerEventListener( barometerEventListener );
        sensorManager.registerDistanceEventListener( distanceEventListener );
        sensorManager.registerGyroscopeEventListener( gyroscopeEventListener, SampleRate.MS128 );
        sensorManager.registerPedometerEventListener( pedometerEventListener );
        sensorManager.registerSkinTemperatureEventListener( skinTempEventListener );
        sensorManager.registerUVEventListener( ultravioletEventListener );
        sensorManager.registerContactEventListener( contactEventListener );
        sensorManager.registerCaloriesEventListener( caloriesEventListener );
        sensorManager.registerGsrEventListener( gsrEventListener );

        //heart rate sensor requires explicit user consent and can be rejected
        checkHeartRateConsent( activity );
        return true;
    }

    //***************************************************************
    // private functions
    //***************************************************************/

    private synchronized void checkHeartRateConsent( final Activity activity )
    {
        if( client == null ) return;

        // check current user heart rate consent
        if( client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED )
        {
            registerHeartRateListeners( true );
        }
        else
        {
            // user hasn’t consented, request consent
            client.getSensorManager().requestHeartRateConsent( activity, heartRateConsentListener );
        }
    }

    private synchronized boolean getConnectedBandClient( final Context context ) throws InterruptedException, BandException
    {
        if( client == null )
        {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if( devices.length == 0 )
            {
                if( listener != null )
                {
                    listener.onBandConnectionStatusChanged( BandStatusEventListener.BandConnectionStatus.NOT_PAIRED );
                }
                return false;
            }
            client = BandClientManager.getInstance().create( context, devices[0] );
        }
        else if( ConnectionState.CONNECTED == client.getConnectionState() )
        {
            if( listener != null )
            {
                listener.onBandConnectionStatusChanged( BandStatusEventListener.BandConnectionStatus.CONNECTED );
            }

            return true;
        }

        if( listener != null )
        {
            listener.onBandConnectionStatusChanged( BandStatusEventListener.BandConnectionStatus.CONNECTING );
        }

        return ConnectionState.CONNECTED == client.connect().await();
    }

    private synchronized void registerHeartRateListeners( boolean consentGiven )
    {
        if( consentGiven )
        {
            try
            {
                BandSensorManager sensorManager = client.getSensorManager();
                sensorManager.registerHeartRateEventListener( heartRateEventListener );
                sensorManager.registerRRIntervalEventListener( rrIntervalEventListener );

                if( listener != null )
                {
                    listener.onBandHeartRateConsentStatusChanged( UserConsent.GRANTED );
                }
                return;
            }
            catch( BandException e )
            {
                if( listener != null )
                {
                    listener.onBandHeartRateConsentStatusChanged( UserConsent.UNSPECIFIED );
                }
            }
        }

        if( listener != null )
        {
            listener.onBandHeartRateConsentStatusChanged( UserConsent.DECLINED );
        }
    }

    //***************************************************************
    // event listeners
    //***************************************************************/

    private BandAccelerometerEventListener accelerometerEventListener = new BandAccelerometerEventListener()
    {
        @Override
        public void onBandAccelerometerChanged( final BandAccelerometerEvent event )
        {
            if( event != null )
            {
                bandSensorData.setAccelerometerData( new AccelerometerData( event ) );
            }
        }
    };

    private BandAltimeterEventListener altimeterEventListener = new BandAltimeterEventListener()
    {
        @Override
        public void onBandAltimeterChanged( final BandAltimeterEvent event )
        {
            if( event != null )
            {
                bandSensorData.setAltimeterData( new AltimeterData( event ) );
            }
        }
    };

    private BandAmbientLightEventListener ambientLightEventListener = new BandAmbientLightEventListener()
    {
        @Override
        public void onBandAmbientLightChanged( BandAmbientLightEvent event )
        {
            if( event != null )
            {
                bandSensorData.setAmbientLightData( new AmbientLightData( event ) );
            }
        }
    };

    private BandBarometerEventListener barometerEventListener = new BandBarometerEventListener()
    {
        @Override
        public void onBandBarometerChanged( BandBarometerEvent event )
        {
            if( event != null )
            {
                bandSensorData.setBarometerData( new BarometerData( event ) );
            }
        }
    };

    private BandCaloriesEventListener caloriesEventListener = new BandCaloriesEventListener()
    {
        @Override
        public void onBandCaloriesChanged( BandCaloriesEvent event )
        {
            if( event != null )
            {
                bandSensorData.setCalorieData( new CalorieData( event ) );
            }
        }
    };

    private BandContactEventListener contactEventListener = new BandContactEventListener()
    {
        @Override
        public void onBandContactChanged( BandContactEvent event )
        {
            if( event != null )
            {
                bandSensorData.setContactData( new ContactData( event ) );
            }
        }
    };

    private BandDistanceEventListener distanceEventListener = new BandDistanceEventListener()
    {
        @Override
        public void onBandDistanceChanged( BandDistanceEvent event )
        {
            if( event != null )
            {
                bandSensorData.setDistanceData( new DistanceData( event ) );
            }
        }
    };

    private BandGsrEventListener gsrEventListener = new BandGsrEventListener()
    {
        @Override
        public void onBandGsrChanged( BandGsrEvent event )
        {
            if( event != null )
            {
                bandSensorData.setGsrData( new GsrData( event ) );
            }
        }
    };

    private BandGyroscopeEventListener gyroscopeEventListener = new BandGyroscopeEventListener(){

        @Override
        public void onBandGyroscopeChanged( BandGyroscopeEvent event )
        {
            if( event != null )
            {
                bandSensorData.setGyroscopeData( new GyroscopeData( event ) );
            }
        }
    };

    private BandHeartRateEventListener heartRateEventListener = new BandHeartRateEventListener()
    {
        @Override
        public void onBandHeartRateChanged( BandHeartRateEvent event )
        {
            if( event != null )
            {
                bandSensorData.setHeartRateData( new HeartRateData( event ) );
            }
        }
    };

    private BandPedometerEventListener pedometerEventListener = new BandPedometerEventListener()
    {
        @Override
        public void onBandPedometerChanged( BandPedometerEvent event )
        {
            if( event != null )
            {
                bandSensorData.setPedometerData( new PedometerData( event ) );
            }
        }
    };

    private BandRRIntervalEventListener rrIntervalEventListener = new BandRRIntervalEventListener()
    {
        @Override
        public void onBandRRIntervalChanged( BandRRIntervalEvent event )
        {
            if( event != null )
            {
                bandSensorData.setRrIntervalData( new RrIntervalData( event ) );
            }
        }
    };

    private BandSkinTemperatureEventListener skinTempEventListener = new BandSkinTemperatureEventListener()
    {
        @Override
        public void onBandSkinTemperatureChanged( BandSkinTemperatureEvent event )
        {
            if( event != null )
            {
                bandSensorData.setSkinTemperatureData( new SkinTemperatureData( event ) );
            }
        }
    };

    private BandUVEventListener ultravioletEventListener = new BandUVEventListener()
    {
        @Override
        public void onBandUVChanged( BandUVEvent event )
        {
            if( event != null )
            {
                bandSensorData.setUvIndexData( new UvIndexData( event ) );
            }
        }
    };

    private HeartRateConsentListener heartRateConsentListener = new HeartRateConsentListener()
    {
        @Override
        public void userAccepted( boolean consentGiven )
        {
            registerHeartRateListeners( consentGiven );
        }
    };

    //***************************************************************
    // private internal classes
    //***************************************************************/

    private class BandConnectionTask extends AsyncTask<Activity, Void, Void>
    {
        @Override
        protected Void doInBackground( Activity... activities )
        {
            BandStatusEventListener.BandConnectionStatus newStatus;
            try
            {
                if( getConnectedBandClient( activities[0] ) )
                {
                    newStatus = BandStatusEventListener.BandConnectionStatus.CONNECTED;
                    startAllSensors( activities[0] );
                }
                else
                {
                    newStatus = BandStatusEventListener.BandConnectionStatus.NOT_CONNECTED;
                }
            }
            catch( BandException e )
            {
                switch( e.getErrorType() )
                {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        newStatus = BandStatusEventListener.BandConnectionStatus.SDK_ERROR;
                        break;
                    case SERVICE_ERROR:
                        newStatus = BandStatusEventListener.BandConnectionStatus.SERVICE_ERROR;
                        break;
                    default:
                        newStatus = BandStatusEventListener.BandConnectionStatus.UNKNOWN;
                        break;
                }

            }
            catch( Exception e )
            {
                newStatus = BandStatusEventListener.BandConnectionStatus.UNKNOWN;
            }

            synchronized( this )
            {
                if( listener != null )
                {
                    listener.onBandConnectionStatusChanged( newStatus );
                }
            }

            return null;
        }
    }
}
