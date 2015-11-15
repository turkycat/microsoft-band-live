package turkycat.microsoftbandlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microsoft.band.UserConsent;

public class MainActivity extends AppCompatActivity implements BandSensorsEventListener
{
    public static final String TAG = "MainActivity";


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
    private TextView gsrData;
    private TextView rrData;

    //control fields
    private boolean enabled;



    //***************************************************************
    // public functions
    //***************************************************************/

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
        gsrData = (TextView) findViewById( R.id.gsr_data );
        rrData = (TextView) findViewById( R.id.rr_data );


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



    private void setEnabled( boolean enabled )
    {
        this.enabled = enabled;

        if( enabled )
        {
            enableButton.setText( R.string.button_text_enabled );

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


    @Override
    public void onBandConnectionStatusChanged( BandConnectionStatus status )
    {
        switch( status )
        {
            case NOT_PAIRED:
                appendToUI( statusText, "Band isn't paired with your phone." );
                break;

            case CONNECTING:
                appendToUI( statusText, "Band is connecting..." );
                break;

            case CONNECTED:
                appendToUI( statusText, "Band is connected." );
                break;

            case NOT_CONNECTED:
                appendToUI( statusText, "Band isn't connected. Please make sure bluetooth is on and the band is in range." );
                break;

            case SDK_ERROR:
                appendToUI( statusText, "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK." );
                break;

            case SERVICE_ERROR:
                appendToUI( statusText, "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions." );
                break;
        }
    }

    @Override
    public void onBandHeartRateConsentStatusChanged(  UserConsent consent )
    {
        String consentMessage;
        switch( consent )
        {
            case UNSPECIFIED:
                consentMessage = "An unknown error occurred.";
                appendToUI( new TextView[] { heartRateRateData, heartRateLockedData },
                        new String[] { consentMessage, consentMessage });
                break;

            case DECLINED:
                consentMessage = "Heart rate consent rejected.";
                appendToUI( new TextView[] { heartRateRateData, heartRateLockedData },
                        new String[] { consentMessage, consentMessage });
                break;

            case GRANTED:

                break;
        }
    }
}
