package turkycat.microsoftbandlive;

/**
 * Created by turkycat on 11/15/2015.
 */
public interface BandSensorsEventListener
{
    enum BandConnectionStatus
    {
        UNKNOWN,
        NOT_PAIRED,
        NOT_CONNECTED,
        CONNECTING,
        CONNECTED
    };

    void onBandConnectionStatusChanged( BandConnectionStatus status );
}
