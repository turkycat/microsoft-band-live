package turkycat.microsoftbandlive;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandAltimeterEvent;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.BandAmbientLightEvent;
import com.microsoft.band.sensors.BandAmbientLightEventListener;
import com.microsoft.band.sensors.BandBarometerEvent;
import com.microsoft.band.sensors.BandBarometerEventListener;
import com.microsoft.band.sensors.BandSensorManager;
import com.microsoft.band.sensors.SampleRate;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = "MainActivity";

    //current Band client or null
    private BandClient client = null;

    //views
    private ScrollView layoutBandData;
    private Button enableButton;
    private TextView statusText;
    private TextView accelerometerData;
    private TextView altimeterRateData;
    private TextView altimeterGainData;
    private TextView altimeterLossData;
    private TextView ambientLightData;
    private TextView barometerPressureData;
    private TextView barometerTempData;

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
                appendToUI( accelerometerData, String.format( " X = %.3f \n Y = %.3f\n Z = %.3f", event.getAccelerationX(),
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
                        new String[] { String.format( "%.2f kPa", event.getAirPressure() ), String.format( "%.2f F", convertCelciusToFahrenheit( event.getTemperature() ) ) });
            }
        }
    };

    //***************************************************************
    // protected functions
    //***************************************************************/

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        layoutBandData = (ScrollView) findViewById( R.id.layout_band_data );
        statusText = (TextView) findViewById( R.id.status_text );
        enableButton = (Button) findViewById( R.id.enable_button );
        accelerometerData = (TextView) findViewById( R.id.accelerometer_data );
        altimeterRateData = (TextView) findViewById( R.id.altimeter_rate_data );
        altimeterGainData = (TextView) findViewById( R.id.altimeter_gain_data );
        altimeterLossData = (TextView) findViewById( R.id.altimeter_loss_data );
        ambientLightData = (TextView) findViewById( R.id.ambientlight_data );
        barometerPressureData = (TextView) findViewById( R.id.barometer_pressure_data );
        barometerTempData = (TextView) findViewById( R.id.barometer_temp_data );


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
