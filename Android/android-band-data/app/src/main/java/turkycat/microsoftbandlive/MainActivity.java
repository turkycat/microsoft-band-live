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

public class MainActivity extends AppCompatActivity implements HeartRateConsentListener
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
    //***************************************************************/btbbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahbtgahtbtgahgbtgahbtgahbtgahbtgahbtgahbtgahbbtgahbtgahbtgahbtgahbtgahbtgahtgbbtgahbtgahtgbbtgahbtgahbtgahbtgahbtgahtgbtgbtgbtgahbtgahbtgahbtgahtgbtgbtgahbtgahbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtggbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtggbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtgbtg
    /**
     * callback for HeartRateConsentListener
     * @param consentGiven
     */
    @Override
    public void userAccepted( boolean consentGiven )
    {
        registerHeartRateListeners( consentGiven );
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


}
