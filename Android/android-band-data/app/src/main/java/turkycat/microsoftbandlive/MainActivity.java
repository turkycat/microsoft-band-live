package turkycat.microsoftbandlive;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = "MainActivity";

    //current Band client or null
    private BandClient client = null;

    //views
    private TextView statusText;
    private Button enableButton;

    //control fields
    private boolean enabled;

    //event listeners


    private BandAccelerometerEventListener mAccelerometerEventListener = new BandAccelerometerEventListener()
    {
        @Override
        public void onBandAccelerometerChanged( final BandAccelerometerEvent event )
        {
            if( event != null )
            {
                appendToUI( statusText, String.format( " X = %.3f \n Y = %.3f\n Z = %.3f", event.getAccelerationX(),
                        event.getAccelerationY(), event.getAccelerationZ() ) );
            }
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        statusText = (TextView) findViewById( R.id.status_text );
        enableButton = (Button) findViewById( R.id.enable_button );

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


    private boolean getConnectedBandClient() throws InterruptedException, BandException
    {
        if( client == null )
        {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if( devices.length == 0 )
            {
                appendToUI( statusText, "Band isn't paired with your phone.\n" );
                return false;
            }
            client = BandClientManager.getInstance().create( getBaseContext(), devices[0] );
        } else if( ConnectionState.CONNECTED == client.getConnectionState() )
        {
            return true;
        }

        appendToUI( statusText, "Band is connecting...\n" );
        return ConnectionState.CONNECTED == client.connect().await();
    }


    private void setEnabled( boolean enabled )
    {
        this.enabled = enabled;

        if( enabled )
        {
            enableButton.setText( R.string.enabled_text );

            new AccelerometerSubscriptionTask().execute();
        } else
        {
            enableButton.setText( R.string.disabled_text );
        }
    }

    private class AccelerometerSubscriptionTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground( Void... params )
        {
            try
            {
                if( getConnectedBandClient() )
                {
                    appendToUI( statusText, "Band is connected.\n" );
                    client.getSensorManager().registerAccelerometerEventListener( mAccelerometerEventListener, SampleRate.MS128 );
                } else
                {
                    appendToUI( statusText, "Band isn't connected. Please make sure bluetooth is on and the band is in range.\n" );
                }
            } catch( BandException e )
            {
                String exceptionMessage = "";
                switch( e.getErrorType() )
                {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
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
