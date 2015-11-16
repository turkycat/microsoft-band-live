package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandCaloriesEvent;

/**
 * Created by turkycat on 11/15/2015.
 */
public class CalorieData
{
    private final long calories;

    public CalorieData( long calories )
    {
        this.calories = calories;
    }

    public CalorieData( BandCaloriesEvent event )
    {
        if( event == null ) throw new IllegalArgumentException();
        this.calories = event.getCalories();
    }

    public long getCalories()
    {
        return calories;
    }
}
