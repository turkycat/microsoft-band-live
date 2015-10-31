package turkycat.microsoftbandlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = "MainActivity";

    private Button enableButton;
    private boolean enabled;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        enableButton = (Button) findViewById( R.id.enable_button );
        setEnabled( false );
    }

    //***************************************************************
    // private functions
    //***************************************************************/

    public void setEnabled( boolean enabled )
    {
        this.enabled = enabled;

        if( enabled )
        {
            enableButton.setText( R.string.enabled_text );
        }
        else
        {
            enableButton.setText( R.string.disabled_text );
        }
    }
}
