package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.UserConsent;

/**
 * Created by turkycat on 11/15/2015.
 */
public interface BandStatusEventListener
{
    enum BandConnectionStatus
    {
        UNKNOWN,
        SDK_ERROR,
        SERVICE_ERROR,
        NOT_PAIRED,
        NOT_CONNECTED,
        CONNECTING,
        CONNECTED
    };

    void onBandConnectionStatusChanged( BandConnectionStatus status );

    void onBandHeartRateConsentStatusChanged( UserConsent consent );
}
