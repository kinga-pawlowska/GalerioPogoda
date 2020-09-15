package kingapawowska.galeriopogoda.adapter;

/**
 * Created by abastra on 07.09.16.
 */
public class Main {
    double mainTemp;
    double mainPressure;
    double mainHumidity;
    double mainTempMin;
    double mainTempMax;

    public Main(double temp, double pressure, double humidity, double tempMin, double tempMax)
    {
        mainTemp = temp;
        mainPressure = pressure;
        mainHumidity = humidity;
        mainTempMin = tempMin;
        mainTempMax = tempMax;
    }
}
