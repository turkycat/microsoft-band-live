package turkycat.microsoftbandlive;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
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
 * A thread-safe class manage Microsoft Band sensor registration and manage sensor data
 * Created by turkycat on 11/15/2015.
 */
public class BandSensors implements HeartRateConsentListener
{
    private Looper looper;

    private BandSensorsEventListener listener;

    //current Band client or null
    private BandClient client = null;

    //***************************************************************
    // constructors
    //***************************************************************/

    public BandSensors()
    {

    }

    //***************************************************************
    // public functions
    //***************************************************************/

    public void initialize( Context context )
    {
        looper = context.getMainLooper();

        new BandConnectionTask().execute();
    }

    public void registerBandSensorsEventListener( BandSensorsEventListener listener )
    {
        this.listener = listener;
    }


    public void startAllSensors() throws BandIOException
    {
        if( client == null ) return;

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
        checkHeartRateConsent();
    }


    /* callback for HeartRateConsentListener
     * @param consentGiven
     */
    @Override
    public void userAccepted( boolean consentGiven )
    {
        registerHeartRateListeners( consentGiven );
    }

    //***************************************************************
    // private functions
    //***************************************************************/

    private void checkHeartRateConsent()
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
            client.getSensorManager().requestHeartRateConsent( this, this );
        }
    }

    private boolean getConnectedBandClient( Context context ) throws InterruptedException, BandException
    {
        if( client == null )
        {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if( devices.length == 0 )
            {
                if( listener != null )
                {
                    listener.onBandConnectionStatusChanged( BandSensorsEventListener.BandConnectionStatus.NOT_PAIRED );
                }
                return false;
            }
            client = BandClientManager.getInstance().create( context, devices[0] );
        } else if( ConnectionState.CONNECTED == client.getConnectionState() )
        {
            return true;
        }

        if( listener != null )
        {
            listener.onBandConnectionStatusChanged( BandSensorsEventListener.BandConnectionStatus.CONNECTING );
        }
        return ConnectionState.CONNECTED == client.connect().await();
    }

    private void registerHeartRateListeners( boolean consentGiven )
    {
        if( consentGiven )
        {
            try
            {
                BandSensorManager sensorManager = client.getSensorManager();
                sensorManager.registerHeartRateEventListener( heartRateEventListener );
                sensorManager.registerRRIntervalEventListener( rrIntervalEventListener );
                return;
            }
            catch( BandException e )
            {
                //do nothing
            }
        }

        String consentMessage = "Heart rate consent rejected.";
        appendToUI( new TextView[] { heartRateRateData, heartRateLockedData },
                new String[] { consentMessage, consentMessage });
    }

    //event listeners
    private BandAccelerometerEventListener accelerometerEventListener = new BandAccelerometerEventListener()
    {
        @Override
        public void onBandAccelerometerChanged( final BandAccelerometerEvent event )
        {
            if( event != null )
            {
                appendToUI( accelerometerData, String.format( "%.3f\n%.3f\n%.3f", event.getAccelerationX(),
                        event.getAccelerationY(), event.getAccelerationZ() ) );
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
                appendToUI( new TextView[] { altimeterRateData, altimeterGainData, altimeterLossData },
                        new String[]{ "" + event.getRate(), "" + event.getTotalGain(), "" + event.getTotalLoss() } );
            }
        }
    };

    private BandAmbientLightEventListener ambientLightEventListener = new BandAmbientLightEventListener()
    {
        @Override
        public void onBandAmbientLightChanged( BandAmbientLightEvent event )
        {
            appendToUI( ambientLightData, String.format( "%d lux", event.getBrightness() ) );
        }
    };

    private BandBarometerEventListener barometerEventListener = new BandBarometerEventListener()
    {
        @Override
        public void onBandBarometerChanged( BandBarometerEvent event )
        {
            if( event != null )
            {
                appendToUI( new TextView[] { barometerPressureData, barometerTempData },
                        new String[] {String.format( "%.2f kPa", event.getAirPressure() ),
                                String.format( "%.2f F", convertCelciusToFahrenheit( event.getTemperature() ) ) });
            }
        }
    };

    private BandDistanceEventListener distanceEventListener = new BandDistanceEventListener()
    {
        @Override
        public void onBandDistanceChanged( BandDistanceEvent event )
        {
            appendToUI( new TextView[] { distanceTotalData, distanceSpeedData, distancePaceData, distanceModeData },
                    new String[] { String.format( "%.2f m", event.getTotalDistance() / 1000.0 ),
                            String.format( "%.2f m/s", event.getSpeed() / 1000.0 ),
                            String.format( "%.2f s/m", event.getPace() / 1000.0 ),
                            event.getMotionType().toString() } );
        }
    };

    private BandGyroscopeEventListener gyroscopeEventListener = new BandGyroscopeEventListener(){

        @Override
        public void onBandGyroscopeChanged( BandGyroscopeEvent event )
        {
            appendToUI( new TextView[] { gyroscopeAccelData, gyroscopeAngularData },
                    new String[] { String.format( "%.3f\n%.3f\n%.3f", event.getAccelerationX(), event.getAccelerationY(), event.getAccelerationZ() ),
                            String.format( "%.3f\n%.3f\n%.3f", event.getAngularVelocityX(), event.getAngularVelocityY(), event.getAngularVelocityZ() ) } );
        }
    };

    private BandHeartRateEventListener heartRateEventListener = new BandHeartRateEventListener()
    {
        @Override
        public void onBandHeartRateChanged( BandHeartRateEvent event )
        {
            appendToUI( new TextView[] { heartRateRateData, heartRateLockedData },
                    new String[] { "" + event.getHeartRate(),
                            event.getQuality().toString() } );
        }
    };

    private BandPedometerEventListener pedometerEventListener = new BandPedometerEventListener()
    {
        @Override
        public void onBandPedometerChanged( BandPedometerEvent event )
        {
            appendToUI( pedometerData, "" + event.getTotalSteps() );
        }
    };

    private BandSkinTemperatureEventListener skinTempEventListener = new BandSkinTemperatureEventListener()
    {
        @Override
        public void onBandSkinTemperatureChanged( BandSkinTemperatureEvent event )
        {
            appendToUI( skinTempData, String.format( "%.2f", convertCelciusToFahrenheit( event.getTemperature() ) ) );
        }
    };

    private BandUVEventListener ultravioletEventListener = new BandUVEventListener()
    {
        @Override
        public void onBandUVChanged( BandUVEvent event )
        {
            appendToUI( ultravioletData, event.getUVIndexLevel().toString() );
        }
    };

    private BandContactEventListener contactEventListener = new BandContactEventListener()
    {
        @Override
        public void onBandContactChanged( BandContactEvent event )
        {
            appendToUI( contactData, event.getContactState().toString() );
        }
    };

    private BandCaloriesEventListener caloriesEventListener = new BandCaloriesEventListener()
    {
        @Override
        public void onBandCaloriesChanged( BandCaloriesEvent event )
        {
            appendToUI( calorieData, "" + event.getCalories() );
        }
    };

    private BandGsrEventListener gsrEventListener = new BandGsrEventListener()
    {
        @Override
        public void onBandGsrChanged( BandGsrEvent event )
        {
            appendToUI( gsrData, "" + event.getResistance() );
        }
    };

    private BandRRIntervalEventListener rrIntervalEventListener = new BandRRIntervalEventListener()
    {
        @Override
        public void onBandRRIntervalChanged( BandRRIntervalEvent event )
        {
            appendToUI( rrData, String.format( "%.2f", convertCelciusToFahrenheit( event.getInterval() ) ) );
        }
    };
    //***************************************************************
    // private internal classes
    //***************************************************************/

    private class BandConnectionTask extends AsyncTask<Context, Void, Void>
    {
        @Override
        protected Void doInBackground( Context... context )
        {
            try
            {
                if( getConnectedBandClient( context ) )
                {
                    appendToUI( statusText, "Band is connected." );
                    registerSensorListeners();
                } else
                {
                    appendToUI( statusText, "Band isn't connected. Please make sure bluetooth is on and the band is in range." );
                }
            } catch( BandException e )
            {
                String exceptionMessage = "";
                switch( e.getErrorType() )
                {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage();
                        break;
                }
                appendToUI( statusText, exceptionMessage );

            } catch( Exception e )
            {
                appendToUI( statusText, e.getMessage() );
            }
            return null;
        }
    }
}
