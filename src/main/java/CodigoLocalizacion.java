import com.google.gson.Gson;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.inject.internal.cglib.core.$LocalVariablesSorter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.Arrays;

public class CodigoLocalizacion {

    public String doHttpGetLocation(String location){

        String info="";

        String enlace = "http://dataservice.accuweather.com/locations/v1/cities/autocomplete?apikey=%09XQIK9hPy36y7kcJOEHhACXsVfR9Uty9c&q="+location+"&language=es";

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

        String[] partes = info.split("\\,");
        System.out.println(partes[1]);
        partes[1] = partes[1].substring(7,partes[1].length()-1);
        System.out.println(partes[1]);


        System.out.println(info);

        return partes[1];

    }

}
