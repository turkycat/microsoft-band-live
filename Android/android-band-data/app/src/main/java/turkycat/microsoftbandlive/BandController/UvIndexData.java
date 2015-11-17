package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandUVEvent;
import com.microsoft.band.sensors.UVIndexLevel;

/**
 * an immutable class to store current data from the ultraviolet sensor
 */
public class UvIndexData
{
    private final UVIndexLevel uvIndexLevel;

    public UvIndexData()
    {
        this( UVIndexLevel.NONE );
    }

    public UvIndexData( UVIndexLevel uvIndexLevel )
    {
        this.uvIndexLevel = uvIndexLevel;
    }

    public UvIndexData( BandUVEvent event )
    {
        this.uvIndexLevel = event.getUVIndexLevel();
    }

    public UVIndexLevel getUVIndexLevel()
    {
        return uvIndexLevel;
    }
}
