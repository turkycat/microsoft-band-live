package turkycat.microsoftbandlive.BandController;

import com.microsoft.band.sensors.BandCaloriesEvent;

/**
 * an immutable class to store current calorie data
 */
public class CalorieData
{
    private final long calories;

    public CalorieData()
    {
        this( 0 );
    }

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
