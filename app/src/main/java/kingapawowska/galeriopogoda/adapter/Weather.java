package kingapawowska.galeriopogoda.adapter;

/**
 * Created by abastra on 07.09.16.
 */
public class Weather {
    int weatherId;
    String weatherMain;
    String weatherDescription;

    public Weather(int id, String main, String description)
    {
        weatherId = id;
        weatherMain = main;
        weatherDescription = description;
    }

}
