
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;


public class CodigoLocalizacion {

    public String doHttpGetLocation(String location){

        String info="";

        String enlace = "http://dataservice.accuweather.com/locations/v1/cities/autocomplete?apikey=YOUR-API-KEY"+location+"&language=es";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(enlace);
        CloseableHttpResponse resp;

        try {

            resp = client.execute(get);
            HttpEntity respuesta = resp.getEntity();
            info = EntityUtils.toString((respuesta));


        } catch (IOException ioe){
            System.err.println("Algo salio mal...");
            ioe.printStackTrace();
        }
        catch (Exception e){
            System.out.println("Error desconocido");
            e.printStackTrace();
        }
        info = info.substring(1,info.length()-1);
        String[] partes = info.split("\\,");
        partes[1] = partes[1].substring(7,partes[1].length()-1);
        return partes[1];
    }

}
