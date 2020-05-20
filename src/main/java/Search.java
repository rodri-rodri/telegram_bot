import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Prints a list of videos based on a search term.
 *
 * @author Jeremy Walker
 */
public class Search {

    /** Global instance properties filename. */
    private static String PROPERTIES_FILENAME = "youtube.properties";

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 3;

    /** Global instance of Youtube object to make all API requests. */
    private static YouTube youtube;


    /* Hago esta clase (viene como main) que retorne un Array de dos dimensiones en los que almaceno:
    * ID del video
    * Nombre del video
    * Tambien le digo que reciba como parametro la busqueda del video que recibe el bot*/
    public String[][] Busqueda (String busquedaVideo) {

        // Read the developer key from youtube.properties
        Properties properties = new Properties();
        /*Creo array de dos dimensiones y le doy el tamaño en funcion de los videos que quiero que me devuelva*/
        String[][] resultadoBusqueda= new String[3][2];

        try {
            InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            /*
             * The YouTube object is used to make all API requests. The last argument is required, but
             * because we don't need anything initialized when the HttpRequest is initialized, we override
             * the interface and provide a no-op function.
             */
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Get query term from user.

            /*Aqui habia una especie de Scanner asique meto la busqueda del video aqui*/
            String queryTerm = busquedaVideo;

            YouTube.Search.List search = youtube.search().list("id,snippet");

            /*
             * It is important to set your developer key from the Google Developer Console for
             * non-authenticated requests (found under the API Access tab at this link:
             * code.google.com/apis/). This is good practice and increased your quota.
             */
            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            search.setQ(queryTerm);
            /*
             * We are only searching for videos (not playlists or channels). If we were searching for
             * more, we would add them as a string like this: "video,playlist,channel".
             */
            search.setType("video");
            /*
             * This method reduces the info returned to only the fields we need and makes calls more
             * efficient.
             */
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null) {
                /*aqui meto la info que me devuelve la busqueda en el array*/
                resultadoBusqueda = prettyPrint(searchResultList.iterator(), queryTerm);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        /*Retorno la info del video*/
        return resultadoBusqueda;
    }

    /*
     * Returns a query term (String) from user via the terminal.
     */
    private static String getInputQuery() throws IOException {

        String inputQuery = "";

        System.out.print("Please enter a search term: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        inputQuery = bReader.readLine();

        if (inputQuery.length() < 1) {
            // If nothing is entered, defaults to "YouTube Developers Live."
            inputQuery = "YouTube Developers Live";
        }
        return inputQuery;
    }

    /*
     * Prints out all SearchResults in the Iterator. Each printed line includes title, id, and
     * thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    private String[][] prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        /*Este metodo solo pinta la info que devuelve la busqueda
        * lo dejo para ver en consola las busquedas que se hagan por el bot
        * */

        String[][] infoVideo = new String[3][2];

        System.out.println("\n=============================================================");
        System.out.println(
                 NUMBER_OF_VIDEOS_RETURNED + " resultados para \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        int contadorVideos=0;

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

//            videoId = rId.getVideoId();

            infoVideo[contadorVideos][0]=rId.getVideoId();
            infoVideo[contadorVideos][1]=singleVideo.getSnippet().getTitle();

            contadorVideos++;

            // Double checks the kind is video.
            if (rId.getKind().equals("youtube#video")) {

                System.out.println(" Video Id " + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
        return infoVideo;
    }
}

