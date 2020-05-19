import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Meteorologia {

    public Tiempo doHttpGet(){

        String info="";
        String enlace = "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/308526?apikey=XQIK9hPy36y7kcJOEHhACXsVfR9Uty9c&language=es&metric=true";

        /*ENLACES
         *CON MUCHA INFO:
         *  "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/308526?apikey=XQIK9hPy36y7kcJOEHhACXsVfR9Uty9c&language=es&details=true&metric=true"
         *CON POCA INFO:
         *  "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/308526?apikey=XQIK9hPy36y7kcJOEHhACXsVfR9Uty9c&language=es&metric=true"   */


        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(enlace);
        CloseableHttpResponse resp;

        try {

            resp = client.execute(get);
            HttpEntity respuesta = resp.getEntity();
            System.out.println("JSON response");
            info = EntityUtils.toString((respuesta));
            System.out.println(info);


        } catch (IOException ioe){
            System.err.println("Algo salio mal...");
            ioe.printStackTrace();
        }
        catch (Exception e){
            System.out.println("Error desconocido");
            e.printStackTrace();
        }

        info = info.substring(1,info.length()-1);

        Tiempo tiempo = new Gson().fromJson(info, Tiempo.class);

        return tiempo;
    }

}



class Tiempo {

    /*
    * EN ESTA CLASE CREO LOS PARAMETROS QUE DEVOLVERA EL JSON*/

    private String DateTime;
    int EpochDateTime;
    int WeatherIcon;
    String IconPhrase;
    boolean HasPrecipitation;
    boolean IsDaylight;
    private temperatures Temperature;
    private temperatures Temperatures = new Gson().fromJson(String.valueOf(Temperature), temperatures.class);
    int PrecipitationProbability;
    String MobileLink;
    String Link;

    public String getDateTime() {
        return DateTime;
    }

    public temperatures getTemperature() {
        return Temperature;
    }

    public void setTemperature(temperatures temperature) {
        Temperature = temperature;
    }

    public temperatures getTemperatures() {
        return Temperatures;
    }

    public void setTemperatures(temperatures temperatures) {
        Temperatures = temperatures;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public int getEpochDateTime() {
        return EpochDateTime;
    }

    public void setEpochDateTime(int epochDateTime) {
        EpochDateTime = epochDateTime;
    }

    public int getWeatherIcon() {
        return WeatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        WeatherIcon = weatherIcon;
    }

    public String getIconPhrase() {
        return IconPhrase;
    }

    public void setIconPhrase(String iconPhrase) {
        IconPhrase = iconPhrase;
    }

    public boolean isHasPrecipitation() {
        return HasPrecipitation;
    }

    public void setHasPrecipitation(boolean hasPrecipitation) {
        HasPrecipitation = hasPrecipitation;
    }

    public boolean isDaylight() {
        return IsDaylight;
    }

    public void setDaylight(boolean daylight) {
        IsDaylight = daylight;
    }


    public int getPrecipitationProbability() {
        return PrecipitationProbability;
    }

    public void setPrecipitationProbability(int precipitationProbability) {
        PrecipitationProbability = precipitationProbability;
    }

    public String getMobileLink() {
        return MobileLink;
    }

    public void setMobileLink(String mobileLink) {
        MobileLink = mobileLink;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    @Override
    public String toString() {
        return "Tiempo{" +
                "DateTime='" + DateTime + '\'' +
                ", EpochDateTime=" + EpochDateTime +
                ", WeatherIcon=" + WeatherIcon +
                ", IconPhrase='" + IconPhrase + '\'' +
                ", HasPrecipitation=" + HasPrecipitation +
                ", IsDaylight=" + IsDaylight +
                ", Temperature=" + Temperature +
                ", Temperatures=" + Temperatures +
                ", PrecipitationProbability=" + PrecipitationProbability +
                ", MobileLink='" + MobileLink + '\'' +
                ", Link='" + Link + '\'' +
                '}';
    }
}

class temperatures {
    double Value;
    String Unit;
    int UnitType;

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getUnitType() {
        return UnitType;
    }

    public void setUnitType(int unitType) {
        UnitType = unitType;
    }

    @Override
    public String toString() {
        return "temperature{" +
                "Value=" + Value +
                ", Unit='" + Unit + '\'' +
                ", UnitType=" + UnitType +
                '}';
    }
}