package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactState;

/**
 * an immutable class to store current contact data
 */
public class ContactData
{
    private final BandContactState state;

    public ContactData()
    {
        this( BandContactState.UNKNOWN );
    }

    public ContactData( BandContactState state )
    {
        this.state = state;
    }

    public ContactData( BandContactEvent event )
    {
        if( event == null ) throw new IllegalArgumentException();
        this.state = event.getContactState();
    }

    public BandContactState getContactState()
    {
        return state;
    }
}
