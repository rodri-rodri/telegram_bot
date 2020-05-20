import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

public class Meteorologia{

    /*Importante, iniciar esta clase con el metodo doHttpGet para que recoja la info que envia la peticion
    * y la almacene en la clase tiempo, asi solo hara una peticion a la web al contrario que si llamase
    * al metodo doHttpGet igualando variables en la clase bot que haria una peticion por variable */

    /*Clase con la que obtengo la info de la web y la almaceno en variables de la clase tiempo*/



    public Tiempo doHttpGet(String codLocation){

        String info="";

        String enlace = "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/" +
                codLocation +
                "?apikey=XQIK9hPy36y7kcJOEHhACXsVfR9Uty9c&language=es&metric=true";

        /*ENLACES
         *CON MUCHA INFO:
         *  "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/308526?apikey=XQIK9hPy36y7kcJOEHhACXsVfR9Uty9c&language=es&details=true&metric=true"
         *CON POCA INFO:
         *  "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/308526?apikey=XQIK9hPy36y7kcJOEHhACXsVfR9Uty9c&language=es&metric=true"
         * SEGUNDA KEY:
         * "http://dataservice.accuweather.com/forecasts/v1/hourly/1hour/308526?apikey=AYA1UWLP42ki8s1pyNdwDEY4wJBvr8IF&language=es&metric=true"*/


        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(enlace);
        CloseableHttpResponse resp;

        try {

            resp = client.execute(get);
            /*Obtengo respuesta de la Web*/
            HttpEntity respuesta = resp.getEntity();
            /*En la variable info almacen la respuesta de la web*/
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
        /*con el substring quito los corchetes de la info de la web para que el conversor gson lo lea como array y no como objeto
        * y soluciono el fallo Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $*/
        info = info.substring(1,info.length()-1);
        System.out.println(info);
        /*Almaceno la info que me devuelve la web en una clase Tiempo*/
        Tiempo tiempo = new Gson().fromJson(info, Tiempo.class);

        return tiempo;
    }

}

class Tiempo {

    /*
    * EN ESTA CLASE CREO LOS PARAMETROS IGUALES QUE LOS QUE DEVOLVERA LA WEB EN JSON
    *  creo metodos accesores para poder obtener los resultados*/

    private String DateTime;
    int EpochDateTime;
    int WeatherIcon;
    String IconPhrase;
    boolean HasPrecipitation;
    boolean IsDaylight;
    /*como la info de la web tiene un array( "cosa":{ ... ... } me da error:
    * Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 178 path $.Temperature
    * Creo una clase temperature y la convierto con Gson como hice con info*/
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

/*Clase con la que soluciono el problema:
* Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 178 path $.Temperature*/
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