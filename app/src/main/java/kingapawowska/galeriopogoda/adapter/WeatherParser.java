package kingapawowska.galeriopogoda.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abastra on 06.09.16.
 */
public class WeatherParser {

    String      sysCountry;
    Coord       coord;
    Weather     weather;
    Main        main;
    Wind        wind;
    int         cloudsAll;
    int         cod;
    String      name;
    String      message;

    //**********************************************************************************************
    public WeatherParser(String queryReturn) {
        try {
            JSONObject  jObj= new JSONObject(queryReturn);

            cod = parseJsonCod(jObj);

            if (cod == 200)
            {
                name = parseJsonName(jObj);
                sysCountry = parseJsonSys(jObj);
                weather = parseJsonWeather(jObj);
                coord = parseJsonCoord(jObj);
                main = parseJsonMain(jObj);
                wind = parseJsonWind(jObj);
                cloudsAll = parseJsonClouds(jObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //**********************************************************************************************
    //----------------------------------------------------------------------------------------------
    private String parseJsonName(JSONObject jObj) throws JSONException {
        return jsonHelperGetString(jObj, "name");

    }
    //----------------------------------------------------------------------------------------------
    private int parseJsonCod(JSONObject jObj) throws JSONException {

        return jsonHelperGetInt(jObj, "cod");
    }
    //----------------------------------------------------------------------------------------------
    private String parseJsonSys(JSONObject jObj) throws JSONException {

        jObj = jsonHelperGetJSONObject(jObj, "sys");
        return jsonHelperGetString(jObj, "country");
    }
    //----------------------------------------------------------------------------------------------
    private Coord parseJsonCoord(JSONObject jObj) throws JSONException {

        /*
        jObj = jsonHelperGetJSONObject(jObj, "coord");
        Double lat = jsonHelperGetDouble(jObj, "lat");
        Double lon = jsonHelperGetDouble(jObj, "lon");

        coord = new Coord(lon, lat);

        return coord;
         */

        jObj = jsonHelperGetJSONObject(jObj, "coord");

        return new Coord(jsonHelperGetDouble(jObj, "lat"), (jsonHelperGetDouble(jObj, "lon")));
    }
    //----------------------------------------------------------------------------------------------
    private Weather parseJsonWeather(JSONObject jObj) throws JSONException {

        /*
        JSONArray weatherArray;
        weatherArray = jsonHelperGetJSONArray(jObj, "weather");

        int id = 0;
        String main = null;
        String description = null;

        for(int i=0; i<weatherArray.length(); i++)
        {
            JSONObject thisWeather = weatherArray.getJSONObject(i);

            id = jsonHelperGetInt(thisWeather, "id");
            main = jsonHelperGetString(thisWeather, "main");
            description = jsonHelperGetString(thisWeather, "description");

            weather = new Weather(id, main, description);
        }
         */

        JSONArray weatherArray;
        weatherArray = jsonHelperGetJSONArray(jObj, "weather");

        int id = 0;
        String main = null;
        String description = null;

        for(int i=0; i<weatherArray.length(); i++)
        {
            JSONObject thisWeather = weatherArray.getJSONObject(i);

            id = jsonHelperGetInt(thisWeather, "id");
            main = jsonHelperGetString(thisWeather, "main");
            description = jsonHelperGetString(thisWeather, "description");
        }

        return new Weather(id, main, description);
    }
    //----------------------------------------------------------------------------------------------
    private Main parseJsonMain(JSONObject jObj) throws JSONException {

        /*
        jObj = jsonHelperGetJSONObject(jObj, "main");
        double temp = jsonHelperGetDouble(jObj, "temp");
        double pressure = jsonHelperGetDouble(jObj, "pressure");
        double humidity = jsonHelperGetDouble(jObj, "humidity");
        double tempMin = jsonHelperGetDouble(jObj, "temp_min");
        double tempMax = jsonHelperGetDouble(jObj, "temp_max");

        main = new Main(temp, pressure, humidity, tempMin, tempMax);

        return main;
         */
        jObj = jsonHelperGetJSONObject(jObj, "main");
        double temp = jsonHelperGetDouble(jObj, "temp");
        double pressure = jsonHelperGetDouble(jObj, "pressure");
        double humidity = jsonHelperGetDouble(jObj, "humidity");
        double tempMin = jsonHelperGetDouble(jObj, "temp_min");
        double tempMax = jsonHelperGetDouble(jObj, "temp_max");

        return new Main(temp, pressure, humidity, tempMin, tempMax);
    }
    //----------------------------------------------------------------------------------------------
    private Wind parseJsonWind(JSONObject jObj) throws JSONException {

        /*
        jObj = jsonHelperGetJSONObject(jObj, "wind");
        double speed = jsonHelperGetDouble(jObj, "speed");
        double deg = jsonHelperGetDouble(jObj, "deg");

        wind = new Wind(speed, deg);

        return wind;
         */

        jObj = jsonHelperGetJSONObject(jObj, "wind");
        double speed = jsonHelperGetDouble(jObj, "speed");
        double deg = jsonHelperGetDouble(jObj, "deg");

        return new Wind(speed, deg);
    }
    //----------------------------------------------------------------------------------------------
    private int parseJsonClouds(JSONObject jObj) throws JSONException {

        jObj = jsonHelperGetJSONObject(jObj, "clouds");

        return jsonHelperGetInt(jObj, "all");
    }
    //----------------------------------------------------------------------------------------------
    //**********************************************************************************************

    public String getSysCountry() { return sysCountry; }

    public Double getCoordLon() { return coord.coordLon; }

    public Double getCoordLat() { return coord.coordLat; }

    public int getWeatherId() { return weather.weatherId; }

    public String getWeatherMain() { return weather.weatherMain; }

    public String getWeatherDescription() { return weather.weatherDescription; }

    public Double getMainTemp() { return main.mainTemp; }

    public Double getMainPressure() { return main.mainPressure; }

    public Double getMainHumidity() { return main.mainHumidity; }

    public Double getMainTempMin() { return main.mainTempMin; }

    public Double getMainTempMax() { return main.mainTempMax; }

    public Double getWindSpeed() { return wind.windSpeed; }

    public Double getWindDeg() { return wind.windDeg; }

    public int getCloudsAll() { return cloudsAll; }

    public int getCod() { return cod; }

    public String getName() { return name; }

    public String getMessage() { return message = "cod 404 NOT FOUND"; }

    //**********************************************************************************************
    private int jsonHelperGetInt(JSONObject obj, String k) {

        int i = 0;

        try {
            i = obj.getInt(String.valueOf(k));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return i;
    }

    private double jsonHelperGetDouble(JSONObject obj, String k) {

        double d = 0.0;

        try {
            d = obj.getDouble(String.valueOf(k));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return d;
    }

    private String jsonHelperGetString(JSONObject obj, String k){
        String v = null;
        try {
            v = obj.getString(String.valueOf(k));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    private JSONObject jsonHelperGetJSONObject(JSONObject obj, String k){
        JSONObject o = null;

        try {
            o = obj.getJSONObject(String.valueOf(k));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return o;
    }

    private JSONArray jsonHelperGetJSONArray(JSONObject obj, String k){
        JSONArray a = null;

        try {
            a = obj.getJSONArray(String.valueOf(k));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return a;
    }
}