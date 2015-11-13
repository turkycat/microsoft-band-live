package turkycat.microsoftbandlive;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.microsoft.band.sensors.BandGyroscopeEvent;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandPedometerEvent;
import com.microsoft.band.sensors.BandPedometerEventListener;
import com.microsoft.band.sensors.BandSensorManager;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;
import com.microsoft.band.sensors.BandUVEvent;
import com.microsoft.band.sensors.BandUVEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;

public class MainActivity extends AppCompatActivity implements HeartRateConsentListener
{
    public static final String TAG = "MainActivity";

    //current Band client or null
    private BandClient client = null;

    //views
    private RelativeLayout layoutBandData;
    private Button enableButton;
    private TextView statusText;
    private TextView accelerometerData;
    private TextView altimeterRateData;
    private TextView altimeterGainData;
    private TextView altimeterLossData;
    private TextView ambientLightData;
    private TextView barometerPressureData;
    private TextView barometerTempData;
    private TextView distanceTotalData;
    private TextView distanceSpeedData;
    private TextView distancePaceData;
    private TextView distanceModeData;
    private TextView gyroscopeAccelData;
    private TextView gyroscopeAngularData;
    private TextView heartRateRateData;
    private TextView heartRateLockedData;
    private TextView pedometerData;
    private TextView skinTempData;
    private TextView ultravioletData;
    private TextView contactData;
    private TextView calorieData;

    //control fields
    private boolean enabled;

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

    //***************************************************************
    // public functions
    //***************************************************************/

    /**
     * callback for HeartRateConsentListener
     * @param consentGiven
     */
    @Override
    public void userAccepted( boolean consentGiven )
    {
        registerHeartRateListener( consentGiven );
    }

    //***************************************************************
    // protected functions
    //***************************************************************/

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        layoutBandData = (RelativeLayout) findViewById( R.id.layout_band_data );
        statusText = (TextView) findViewById( R.id.status_text );
        enableButton = (Button) findViewById( R.id.enable_button );
        accelerometerData = (TextView) findViewById( R.id.accelerometer_data );
        altimeterRateData = (TextView) findViewById( R.id.altimeter_rate_data );
        altimeterGainData = (TextView) findViewById( R.id.altimeter_gain_data );
        altimeterLossData = (TextView) findViewById( R.id.altimeter_loss_data );
        ambientLightData = (TextView) findViewById( R.id.ambientlight_data );
        barometerPressureData = (TextView) findViewById( R.id.barometer_pressure_data );
        barometerTempData = (TextView) findViewById( R.id.barometer_temp_data );
        distanceTotalData = (TextView) findViewById( R.id.distance_total_data );
        distanceSpeedData = (TextView) findViewById( R.id.distance_speed_data );
        distancePaceData = (TextView) findViewById( R.id.distance_pace_data );
        distanceModeData = (TextView) findViewById( R.id.distance_mode_data );
        gyroscopeAccelData = (TextView) findViewById( R.id.gyroscope_accel_data );
        gyroscopeAngularData = (TextView) findViewById( R.id.gyroscope_angular_data );
        heartRateRateData = (TextView) findViewById( R.id.heartrate_rate_data );
        heartRateLockedData = (TextView) findViewById( R.id.heartrate_locked_data );
        pedometerData = (TextView) findViewById( R.id.pedometer_data );
        skinTempData = (TextView) findViewById( R.id.skintemp_data );
        ultravioletData = (TextView) findViewById( R.id.ultraviolet_data );
        contactData = (TextView) findViewById( R.id.contact_data );
        calorieData = (TextView) findViewById( R.id.calorie_data );


                                        setEnabled( false );
        enableButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                setEnabled( !enabled );
            }
        } );
    }

    //***************************************************************
    // private functions
    //***************************************************************/


    private void appendToUI( final TextView textView, final String string )
    {
        this.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                textView.setText( string );
            }
        } );
    }

    private void appendToUI( final TextView[] textViews, final String[] strings )
    {
        this.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                int size = textViews.length > strings.length ? strings.length : textViews.length;
                for( int i = 0; i < size; ++i )
                {
                    textViews[i].setText( strings[i] );
                }
            }
        } );
    }

    private void checkHeartRateConsent()
    {
        if( client == null ) return;

        // check current user heart rate consent
        if( client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED )
        {
            registerHeartRateListener( true );
        }
        else
        {
            // user hasn’t consented, request consent
            client.getSensorManager().requestHeartRateConsent( this, this );
        }

    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException
    {
        if( client == null )
        {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if( devices.length == 0 )
            {
                appendToUI( statusText, "Band isn't paired with your phone." );
                return false;
            }
            client = BandClientManager.getInstance().create( getBaseContext(), devices[0] );
        } else if( ConnectionState.CONNECTED == client.getConnectionState() )
        {
            return true;
        }

        appendToUI( statusText, "Band is connecting..." );
        return ConnectionState.CONNECTED == client.connect().await();
    }

    private void registerSensorListeners() throws BandIOException
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

        //heart rate sensor requires explicit user consent and can be rejected
        checkHeartRateConsent();
    }

    private void registerHeartRateListener( boolean consentGiven )
    {
        if( consentGiven )
        {
            try
            {
                client.getSensorManager().registerHeartRateEventListener( heartRateEventListener );
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

    private void setEnabled( boolean enabled )
    {
        this.enabled = enabled;

        if( enabled )
        {
            enableButton.setText( R.string.button_text_enabled );

            new AccelerometerSubscriptionTask().execute();
            layoutBandData.setVisibility( View.VISIBLE );
        } else
        {
            enableButton.setText( R.string.button_text_disabled );
            statusText.setText( R.string.status_text_disabled );
            layoutBandData.setVisibility( View.GONE );

        }
    }

    //***************************************************************
    // private utility functions
    //***************************************************************/

    private double convertCelciusToFahrenheit( double celcius )
    {
        return ( ( celcius * 9.0 ) / 5.0 ) + 32.0;
    }

    //***************************************************************
    // private internal classes
    //***************************************************************/

    private class AccelerometerSubscriptionTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground( Void... params )
        {
            try
            {
                if( getConnectedBandClient() )
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
