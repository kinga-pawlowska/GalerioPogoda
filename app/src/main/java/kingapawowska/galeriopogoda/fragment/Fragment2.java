package kingapawowska.galeriopogoda.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import kingapawowska.galeriopogoda.GPSTracker;
import kingapawowska.galeriopogoda.R;
import kingapawowska.galeriopogoda.adapter.WeatherParser;

/**
 * Created by Kinga on 2016-08-29.
 */
public class Fragment2 extends Fragment {
    public Fragment2() {}

    // GPSTracker class
    GPSTracker gps;

    EditText editTextCityName;
    Button btnByCityName, btnByCoord;
    TextView textViewResult, textViewInfo;
    double latitude=0.0;
    double longitude=0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pogoda, container, false);

        editTextCityName = (EditText) rootView.findViewById(R.id.cityname);
        btnByCityName = (Button) rootView.findViewById(R.id.bycityname);
        btnByCoord = (Button) rootView.findViewById(R.id.btnByCoord);
        textViewResult = (TextView) rootView.findViewById(R.id.result);
        textViewInfo = (TextView) rootView.findViewById(R.id.info);

        btnByCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OpenWeatherMapTask(
                        editTextCityName.getText().toString(),
                        textViewResult).execute();
            }
        });

        btnByCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create class object
                gps = new GPSTracker(getContext());

                // Check if GPS enabled
                if(gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    System.out.println("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    System.out.println("GPS is disabled");
                    gps.showSettingsAlert();
                }
                new OpenWeatherMapTaskByCoord(
                        latitude, longitude, textViewResult).execute();

            }
        });

        return rootView;
    }

    private class OpenWeatherMapTask extends AsyncTask<Void, Void, String> {

        String cityName;
        TextView tvResult;

        String dummyAppid = "cba3c11094c29177fa2fcca99f2cc157";
        String queryWeather = "http://api.openweathermap.org/data/2.5/weather?q=";
        String queryDummyKey = "&appid=" + dummyAppid;

        OpenWeatherMapTask(String cityName, TextView tvResult){
            this.cityName = cityName;
            this.tvResult = tvResult;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            String queryReturn = null;
            WeatherParser weatherParser;

            String query = null;

            try {
                query = queryWeather + URLEncoder.encode(cityName, "UTF-8") + queryDummyKey;
                queryReturn = sendQuery(query);

                weatherParser = new WeatherParser(queryReturn);
                result = displayENG(weatherParser);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                queryReturn = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                queryReturn = e.getMessage();
            }


            final String finalQueryReturn = query + "\n\n" + queryReturn;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewInfo.setText(finalQueryReturn);
                }
            });

            return result;
        }

        protected String displayENG(WeatherParser weather) {
            String result = "";
            String messageCod = weather.getCod() + "";

            if (weather.getCod() != 0)
            {

                if (weather.getCod() == 200)
                {
                    double temperatureCelcius = weather.getMainTemp()-273.15;

                    result = weather.getName() + "(" + weather.getSysCountry() + ")"
                            + "\n\n"
                            + "COORD:" + "\n" + "lon: " + weather.getCoordLon() + "\n"
                            + "lat: " + weather.getCoordLat() + "\n\n"
                            + "WEATHER:" + "\n"
                            + "Id: " + weather.getWeatherId() + "\n"
                            + "Main: " + weather.getWeatherMain() + "\n"
                            + "Description: " + weather.getWeatherDescription() + "\n\n"
                            + "MAIN:" + "\n"
                            + "Temperature [K]: " + weather.getMainTemp() + "\n"
                            + "Temperature [°C]: " + temperatureCelcius + "\n"
                            + "Pressure: " + weather.getMainPressure() + "\n"
                            + "Humidity: " + weather.getMainHumidity() + "\n"
                            + "Min temperature: " + weather.getMainTempMin() + "\n"
                            + "Max temperature: " + weather.getMainTempMax() + "\n\n"
                            + "CLOUDS:" + "\n"
                            + "All: " + weather.getCloudsAll() + "\n\n"
                            + "WIND:" + "\n"
                            + "Speed: " + weather.getWindSpeed() + "\n"
                            + "Deg: " + weather.getWindDeg() + "\n\n";

                }
                else if(weather.getCod() == 404)
                {
                    result = weather.getMessage() + "";
                }
            }
            else
            {
                //result += "cod == null\n";
                result += "NIE WPISANO MIASTA LUB MIASTO NIE ISTNIEJE!\n";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            tvResult.setText(s);
        }

        private String sendQuery(String query) throws IOException {
            String result = "";

            URL searchURL = new URL(query);

            HttpURLConnection httpURLConnection = (HttpURLConnection)searchURL.openConnection();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader,
                        8192);

                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                bufferedReader.close();
            }

            return result;
        }
    }

    private class OpenWeatherMapTaskByCoord extends AsyncTask<Void, Void, String> {

        double doubleLatitude;
        double doubleLongtitude;
        TextView tvResult;

        String dummyAppid = "cba3c11094c29177fa2fcca99f2cc157";
        String queryWeather = "http://api.openweathermap.org/data/2.5/weather";
        String queryDummyKey = "&appid=" + dummyAppid;

        String latString = "?lat=";
        String lonString = "&lon=";

        OpenWeatherMapTaskByCoord(double doubleLatitude, double doubleLongtitude, TextView tvResult){
            this.doubleLatitude = doubleLatitude;
            this.doubleLongtitude = doubleLongtitude;
            this.tvResult = tvResult;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            String queryReturn = null;
            WeatherParser weatherParser;

            String query = null;

            try {
                query = queryWeather + latString + doubleLatitude + lonString + doubleLongtitude + queryDummyKey;
                //query = "http://api.openweathermap.org/data/2.5/weather?lat=51.8628234&lon=19.395393&appid=cba3c11094c29177fa2fcca99f2cc157";
                queryReturn = sendQuery(query);
                System.out.println(query);

                weatherParser = new WeatherParser(queryReturn);
                result = displayENG(weatherParser);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                queryReturn = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                queryReturn = e.getMessage();
            }


            final String finalQueryReturn = query + "\n\n" + queryReturn;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewInfo.setText(finalQueryReturn);
                }
            });

            return result;
        }

        protected String displayENG(WeatherParser weather) {
            String result = "";
            String messageCod = weather.getCod() + "";

            if (weather.getCod() != 0)
            {

                if (weather.getCod() == 200)
                {
                    double temperatureCelcius = weather.getMainTemp()-273.15;

                    result = weather.getName() + "(" + weather.getSysCountry() + ")"
                            + "\n\n"
                            + "COORD:" + "\n" + "lon: " + weather.getCoordLon() + "\n"
                            + "lat: " + weather.getCoordLat() + "\n\n"
                            + "WEATHER:" + "\n"
                            + "Id: " + weather.getWeatherId() + "\n"
                            + "Main: " + weather.getWeatherMain() + "\n"
                            + "Description: " + weather.getWeatherDescription() + "\n\n"
                            + "MAIN:" + "\n"
                            + "Temperature [K]: " + weather.getMainTemp() + "\n"
                            + "Temperature [°C]: " + temperatureCelcius + "\n"
                            + "Pressure: " + weather.getMainPressure() + "\n"
                            + "Humidity: " + weather.getMainHumidity() + "\n"
                            + "Min temperature: " + weather.getMainTempMin() + "\n"
                            + "Max temperature: " + weather.getMainTempMax() + "\n\n"
                            + "CLOUDS:" + "\n"
                            + "All: " + weather.getCloudsAll() + "\n\n"
                            + "WIND:" + "\n"
                            + "Speed: " + weather.getWindSpeed() + "\n"
                            + "Deg: " + weather.getWindDeg() + "\n\n";

                }
                else if(weather.getCod() == 404)
                {
                    result = weather.getMessage() + "";
                }
            }
            else
            {
                result += "cod == null\n";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            tvResult.setText(s);
        }

        private String sendQuery(String query) throws IOException {
            String result = "";

            URL searchURL = new URL(query);

            HttpURLConnection httpURLConnection = (HttpURLConnection)searchURL.openConnection();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader,
                        8192);

                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                bufferedReader.close();
            }

            return result;
        }
    }


}
