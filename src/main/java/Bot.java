import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class Bot extends TelegramLongPollingBot {

    private boolean busquedaAbierta; //BOOLEANO QUE INDICA SI ESTA ACTIVA LA OPCION DE BUSQUEDA
    private boolean mostrarResultadosVideo = false;
    private SendMessage message = new SendMessage();
    private String[][] videoInfo;
    private String descarga;
    private boolean preguntaTiempo;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()){

            long chat_id = update.getMessage().getChatId(); //OBTENGO CHATID EN UNA VARIABLE

            System.out.println(update.getMessage().getFrom().getFirstName()+ " - " + update.getMessage().getText()); //MUESTRO EN CONSOLA NOMBRE Y MENSAJE RECIBIDO

            if (busquedaAbierta) { //SOLO ENTRA PARA LAS BUSQUEDAS DE YOUTUBE CUANDO busquedaAbierta ES TRUE....

                Search search = new Search();
                String busquedaUsuario = update.getMessage().getText();

                //INICIALIZO CLASE SEARCH DE YOUTUBE

                videoInfo= search.Busqueda(busquedaUsuario); //ARRAY BIDIMENSIONAL QUE GUARDA INFO SOBRE ID DEL VIDEO[x][] Y NOMBRE[][x]

                message = new SendMessage().setChatId(chat_id).setText("He encontrado estos videos...");

                //A PARTIR DE AQUI CREAMOS TECLADO TECLADO
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

                //PARA CREAR EL TECLADO INICIO UN ARRAYLIST DE KEYBOARDROWS. CADA VEZ QUE AÑADA .ADD UN ELEMENTO A LA LISTA CREARA UNA FILA
                List<KeyboardRow> tablaBotones = new ArrayList<>();         //INICIO PRIMERA FILA

                KeyboardRow boton = new KeyboardRow();//INICIO LOS BOTONES DE LA PRIMERA FILA -- ES UNA LISTA DE BOTONES POR FILA
                boton.add(videoInfo[0][1]);//AÑADO BOTONES
                tablaBotones.add(boton);//LOS AÑADO A LA TABLA

                boton = new KeyboardRow();
                boton.add(videoInfo[1][1]);
                tablaBotones.add(boton);

                boton = new KeyboardRow();
                boton.add(videoInfo[2][1]);
                tablaBotones.add(boton);

                boton = new KeyboardRow();
                boton.add("VOLVER AL TECLADO");
                tablaBotones.add(boton);


                // ENVIO LA TABLA DE BOTONES
                keyboardMarkup.setKeyboard(tablaBotones);
                // Y LA AÑADO COMO MENSAJE PARA ENVIARLO CON SENDMESSAGE
                message.setReplyMarkup(keyboardMarkup);
                try {
                    sendMessage(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                busquedaAbierta = false;

                mostrarResultadosVideo = true;
            }

            if (mostrarResultadosVideo){
                int contador =0;
                for (int i=0;i<3;i++){

                    if (update.getMessage().getText().equals(videoInfo[i][1])) {

                        descarga = videoInfo[i][0];

                        message = new SendMessage().setChatId(chat_id).setText("https://www.youtube.com/watch?v="+videoInfo[i][0]);

                        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                        List<InlineKeyboardButton> rowInline = new ArrayList<>();
                        rowInline.add(new InlineKeyboardButton().setText("DESCARGAR").setCallbackData("DESCARGA"));
                        rowsInline.add(rowInline);
                        markupInline.setKeyboard(rowsInline);
                        message.setReplyMarkup(markupInline);
                        try {
                            execute(message); // Sending our message object to user
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }

                    } else if (update.getMessage().getText().equals("VOLVER AL TECLADO")) {

                        if (contador==0){
                            SendMessage msg = new SendMessage()
                                    .setChatId(chat_id)
                                    .setText(EmojiParser.parseToUnicode(":alien:"));
                            ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                            msg.setReplyMarkup(keyboardMarkup);
                            mostrarResultadosVideo = false;
                            try {
                                sendMessage(msg); // Call method to send the photo
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        contador++;
                    }
                }
            }



            if (update.getMessage().getText().equals("/youtube")) {

                message = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("¿Que quieres buscar? :smile:"));

                try {
                    sendMessage(message);
                } catch (TelegramApiException e) {

                    System.out.println("NO SE HA ENVIADO EL MENSAJE");
                }

                busquedaAbierta =new Boolean(true);

            }

            if (preguntaTiempo){

                CodigoLocalizacion cod = new CodigoLocalizacion();

                String key = cod.doHttpGetLocation(update.getMessage().getText());

                Meteorologia meteo = new Meteorologia();

                Tiempo tiempo = meteo.doHttpGet(key);
                String precipitacionesString;
                String esDeDiaString;

                if (tiempo.isHasPrecipitation()){
                    precipitacionesString = "Esta lloviendo, pilla paraguas.";
                } else {
                    precipitacionesString = "Cero precipitaciones.";
                }

                if (tiempo.isDaylight()){
                    esDeDiaString = "Es de dia.";
                } else {
                    esDeDiaString = "Es de noche.";
                }

                String mensaje = "La temperatura actual en " +
                        update.getMessage().getText() +
                        " es : \n" +
                        tiempo.getIconPhrase() + ".\n" +
                        tiempo.getTemperature().getValue() + " Cº.\n"+
                        esDeDiaString + "\n" +
                        precipitacionesString + "\n" +
                        "Probabilidad de precipitaciones: " + tiempo.getPrecipitationProbability() + "%. \n" +
                        tiempo.getLink();

                message = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("\t:sunny:"+":umbrella:"+":cloud:"+":snowflake:\n"+mensaje));

                try {
                    sendMessage(message);
                } catch (TelegramApiException e) {

                    System.out.println("NO SE HA ENVIADO EL MENSAJE");
                }

                preguntaTiempo=false;
            }


            if (update.getMessage().getText().equals("/tiempo")){

                message = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("¿De donde quieres bsaber el tiempo?"));
                try {
                    sendMessage(message);
                } catch (TelegramApiException e) {

                    System.out.println("NO SE HA ENVIADO EL MENSAJE");
                }
                preguntaTiempo=true;
            }

        } else if (update.hasCallbackQuery()) {

            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("DESCARGA")) {
                String link = "https://www.yout.com/watch?v="+descarga;
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(link);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "Jason_Voorhees_bot";
    }

    @Override
    public String getBotToken() {
        return "bot-key";
    }
}
