package turkycat.microsoftbandlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microsoft.band.UserConsent;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BandSensorsEventListener
{
    public static final String TAG = "MainActivity";

    //views
    private RelativeLayout layoutBandData;
    private Button enableButton;
    private TextView statusText;
    private HashMap<Integer, TextView> dataTextViews;

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
        enableButton = (Button) findViewById( R.id.enable_button );
        statusText = (TextView) findViewById( R.id.status_text );

        dataTextViews = new HashMap<>();
        dataTextViews.put( R.id.accelerometer_data, (TextView) findViewById( R.id.accelerometer_data ) );
        dataTextViews.put( R.id.altimeter_rate_data, (TextView) findViewById( R.id.altimeter_rate_data ) );
        dataTextViews.put( R.id.altimeter_gain_data, (TextView) findViewById( R.id.altimeter_gain_data ) );
        dataTextViews.put( R.id.altimeter_loss_data, (TextView) findViewById( R.id.altimeter_loss_data ) );
        dataTextViews.put( R.id.ambientlight_data, (TextView) findViewById( R.id.ambientlight_data ) );
        dataTextViews.put( R.id.barometer_pressure_data, (TextView) findViewById( R.id.barometer_pressure_data ) );
        dataTextViews.put( R.id.barometer_temp_data, (TextView) findViewById( R.id.barometer_temp_data ) );
        dataTextViews.put( R.id.distance_total_data, (TextView) findViewById( R.id.distance_total_data ) );
        dataTextViews.put( R.id.distance_speed_data, (TextView) findViewById( R.id.distance_speed_data ) );
        dataTextViews.put( R.id.distance_pace_data, (TextView) findViewById( R.id.distance_pace_data ) );
        dataTextViews.put( R.id.distance_mode_data, (TextView) findViewById( R.id.distance_mode_data ) );
        dataTextViews.put( R.id.gyroscope_accel_data, (TextView) findViewById( R.id.gyroscope_accel_data ) );
        dataTextViews.put( R.id.gyroscope_angular_data, (TextView) findViewById( R.id.gyroscope_angular_data ) );
        dataTextViews.put( R.id.heartrate_rate_data, (TextView) findViewById( R.id.heartrate_rate_data ) );
        dataTextViews.put( R.id.heartrate_locked_data, (TextView) findViewById( R.id.heartrate_locked_data ) );
        dataTextViews.put( R.id.pedometer_data, (TextView) findViewById( R.id.pedometer_data ) );
        dataTextViews.put( R.id.skintemp_data, (TextView) findViewById( R.id.skintemp_data ) );
        dataTextViews.put( R.id.ultraviolet_data, (TextView) findViewById( R.id.ultraviolet_data ) );
        dataTextViews.put( R.id.contact_data, (TextView) findViewById( R.id.contact_data ) );
        dataTextViews.put( R.id.calorie_data, (TextView) findViewById( R.id.calorie_data ) );
        dataTextViews.put( R.id.gsr_data, (TextView) findViewById( R.id.gsr_data ) );
        dataTextViews.put( R.id.rr_data, (TextView) findViewById( R.id.rr_data ) );


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
        String consentMessage = null;
        switch( consent )
        {
            case UNSPECIFIED:
                consentMessage = "An unknown error occurred.";
                break;

            case DECLINED:
                consentMessage = "Heart rate consent rejected.";
                break;

            case GRANTED:

                break;
        }

        if( consentMessage != null )
        {
            appendToUI( new TextView[] { dataTextViews.get( R.id.heartrate_rate_data ), dataTextViews.get( R.id.heartrate_locked_data ) },
                        new String[] { consentMessage, consentMessage });
        }
    }
}
