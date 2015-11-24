package turkycat.microsoftbandlive;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microsoft.band.UserConsent;

import java.util.HashMap;

import turkycat.microsoftbandlive.BandController.AccelerometerData;
import turkycat.microsoftbandlive.BandController.AltimeterData;
import turkycat.microsoftbandlive.BandController.BandController;
import turkycat.microsoftbandlive.BandController.BandSensorData;
import turkycat.microsoftbandlive.BandController.BandStatusEventListener;
import turkycat.microsoftbandlive.BandController.BarometerData;
import turkycat.microsoftbandlive.BandController.DistanceData;
import turkycat.microsoftbandlive.BandController.GyroscopeData;
import turkycat.microsoftbandlive.BandController.HeartRateData;

public class MainActivity extends AppCompatActivity implements BandStatusEventListener
{
    public static final String TAG = "MainActivity";

    private UserInterfaceUpdateTask userInterfaceUpdateTask;

    //band controller
    private BandController bandController;

    //views
    private RelativeLayout layoutBandData;
    private Button enableButton;
    private TextView statusText;
    private HashMap<Integer, TextView> dataTextViews;

    //control fields
    private boolean enabled;

    //***************************************************************
    // protected functions
    //***************************************************************/

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        bandController = new BandController();
        bandController.setBandStatusEventListener( this );

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
    // public functions
    //***************************************************************/

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
                userInterfaceUpdateTask = new UserInterfaceUpdateTask();
                userInterfaceUpdateTask.execute( this );
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

        if( status != BandConnectionStatus.CONNECTED && userInterfaceUpdateTask != null )
        {
            userInterfaceUpdateTask.cancel( true );
            userInterfaceUpdateTask = null;
        }
    }

    @Override
    public void onBandHeartRateConsentStatusChanged( UserConsent consent )
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
            appendToUI( new TextView[]{ dataTextViews.get( R.id.heartrate_rate_data ), dataTextViews.get( R.id.heartrate_locked_data ) },
                    new String[]{ consentMessage, consentMessage } );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );
        return true;
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
            bandController.connect( this );
            enableButton.setText( R.string.button_text_enabled );
            layoutBandData.setVisibility( View.VISIBLE );
        }
        else
        {
            bandController.disconnect();
            enableButton.setText( R.string.button_text_disabled );
            statusText.setText( R.string.status_text_disabled );
            layoutBandData.setVisibility( View.GONE );
        }
    }

    private void updateAllSensorDataFields()
    {
        BandSensorData sensorData = bandController.getBandSensorData();

        //update accelerometer text
        AccelerometerData accelerometerData = sensorData.getAccelerometerData();
        dataTextViews.get( R.id.accelerometer_data ).setText( String.format( "%.3f\n%.3f\n%.3f",
                accelerometerData.getX(),
                accelerometerData.getY(),
                accelerometerData.getZ() ) );

        //update altimeter text
        AltimeterData altimeterData = sensorData.getAltimeterData();
        dataTextViews.get( R.id.altimeter_rate_data ).setText( "" + altimeterData.getRate() );
        dataTextViews.get( R.id.altimeter_gain_data ).setText( "" + altimeterData.getTotalGain() );
        dataTextViews.get( R.id.altimeter_loss_data ).setText( "" + altimeterData.getTotalLoss() );

        //update ambient light text
        dataTextViews.get( R.id.ambientlight_data ).setText( String.format( "%d lux", sensorData.getAmbientLightData().getBrightness() ) );

        //update barometer text
        BarometerData barometerData = sensorData.getBarometerData();
        dataTextViews.get( R.id.barometer_temp_data ).setText( String.format( "%.2f F", barometerData.getTemperatureF() ) );
        dataTextViews.get( R.id.barometer_pressure_data ).setText( String.format( "%.2f kPa", barometerData.getAirPressure() ) );

        //update calorie text
        dataTextViews.get( R.id.calorie_data ).setText( "" + sensorData.getCalorieData().getCalories() );

        //update contact text
        dataTextViews.get( R.id.contact_data ).setText( sensorData.getContactData().getContactState().toString() );

        //update distance text
        DistanceData distanceData = sensorData.getDistanceData();
        dataTextViews.get( R.id.distance_total_data ).setText( String.format( "%.2f m", distanceData.getTotalDistance() / 1000.0 ) );
        dataTextViews.get( R.id.distance_speed_data ).setText( String.format( "%.2f m/s", distanceData.getSpeed() / 1000.0 ) );
        dataTextViews.get( R.id.distance_pace_data ).setText( String.format( "%.2f s/m", distanceData.getPace() / 1000.0 ) );
        dataTextViews.get( R.id.distance_mode_data ).setText( distanceData.getMotionType().toString() );

        //update skin resistance text
        dataTextViews.get( R.id.gsr_data ).setText( "" + sensorData.getGsrData().getResistance() );

        //update gyroscope text
        GyroscopeData gyroscopeData = sensorData.getGyroscopeData();
        dataTextViews.get( R.id.gyroscope_accel_data ).setText( String.format( "%.3f\n%.3f\n%.3f",
                gyroscopeData.getAccelerationX(),
                gyroscopeData.getAccelerationY(),
                gyroscopeData.getAccelerationZ() ) );
        dataTextViews.get( R.id.gyroscope_angular_data ).setText( String.format( "%.3f\n%.3f\n%.3f",
                gyroscopeData.getAngularVelocityX(),
                gyroscopeData.getAngularVelocityY(),
                gyroscopeData.getAngularVelocityZ() ) );

        //update heart rate text
        HeartRateData heartRateData = sensorData.getHeartRateData();
        dataTextViews.get( R.id.heartrate_rate_data ).setText( "" + heartRateData.getHeartRate() );
        dataTextViews.get( R.id.heartrate_locked_data ).setText( heartRateData.getQuality().toString() );

        //update pedometer text
        dataTextViews.get( R.id.pedometer_data ).setText( "" + sensorData.getPedometerData().getTotalSteps() );

        //update rr interval text
        dataTextViews.get( R.id.rr_data ).setText( String.format( "%.2f", sensorData.getRrIntervalData().getInterval() ) );

        //update skin temperature text
        dataTextViews.get( R.id.skintemp_data ).setText( String.format( "%.2f F", sensorData.getSkinTemperatureData().getTemperatureF() ) );

        //update uv index data
        dataTextViews.get( R.id.ultraviolet_data ).setText( sensorData.getUvIndexData().getUVIndexLevel().toString() );
    }

    //***************************************************************
    // private utility functions
    //***************************************************************/

    private class UserInterfaceUpdateTask extends AsyncTask<AppCompatActivity, Void, Void>
    {
        private static final int ENABLED_DELAY_MILLIS = 250;
        private static final int DISABLED_DELAY_MILLIS = 1500;

        @Override
        protected Void doInBackground( AppCompatActivity... activities )
        {
            int delay_millis;
            for( ; ; )
            {
                if( bandController.isConnected() )
                {
                    delay_millis = ENABLED_DELAY_MILLIS;
                    activities[0].runOnUiThread(
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    updateAllSensorDataFields();
                                }
                            }
                    );
                }
                else
                {
                    delay_millis = DISABLED_DELAY_MILLIS;
                }

                try
                {
                    Thread.sleep( delay_millis );
                }
                catch( InterruptedException e )
                {
                    //do nothing
                }
            }
        }
    }
}
