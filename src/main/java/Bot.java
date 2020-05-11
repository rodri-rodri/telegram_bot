import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private boolean busquedaAbierta; //BOOLEANO QUE INDICA SI ESTA ACTIVA LA OPCION DE BUSQUEDA
    private SendMessage message = new SendMessage();
    private String[][] videoInfo;

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(busquedaAbierta);
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

//                boton = new KeyboardRow();
//                boton.add(videoInfo[3][1]);
//                tablaBotones.add(boton);

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


            }


            if (update.getMessage().getText().equals("/youtube")) {

                message = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("¿Que quieres buscar? :smile:"));

                try {
                    sendMessage(message);
                } catch (TelegramApiException e) {

                    System.out.println("NO SE HA ENVIADO EL MENSAJE");
                }

                busquedaAbierta =new Boolean(true);

            } else if (update.getMessage().getText().equals(videoInfo[0][1])){

                message = new SendMessage().setChatId(chat_id).setText("https://www.youtube.com/watch?v="+videoInfo[0][0]);

                try {
                    sendMessage(message);
                } catch (TelegramApiException e) {

                    System.out.println("ERROR: EL VIDEO NO SE HA ENVIADO");

                }
            } else if (update.getMessage().getText().equals(videoInfo[1][1])){

                message = new SendMessage().setChatId(chat_id).setText("https://www.youtube.com/watch?v="+videoInfo[1][0]);

                try {
                    sendMessage(message);
                } catch (TelegramApiException e) {

                    System.out.println("ERROR: EL VIDEO NO SE HA ENVIADO");

                }
            } else if (update.getMessage().getText().equals(videoInfo[2][1])){

                message = new SendMessage().setChatId(chat_id).setText("https://www.youtube.com/watch?v="+videoInfo[2][0]);

                try {
                    sendMessage(message);
                } catch (TelegramApiException e) {

                    System.out.println("ERROR: EL VIDEO NO SE HA ENVIADO");

                }


//            } else if (update.getMessage().getText().equals(videoInfo[3][1])){
//
//                message = new SendMessage().setChatId(chat_id).setText("https://www.youtube.com/watch?v="+videoInfo[3][0]);
//
//                try {
//                    sendMessage(message);
//                } catch (TelegramApiException e) {
//
//                    System.out.println("ERROR: EL VIDEO NO SE HA ENVIADO");
//
//                }

            } else if (update.getMessage().getText().equals("VOLVER AL TECLADO")) {
                SendMessage msg = new SendMessage()
                        .setChatId(chat_id)
                        .setText(":-)");
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                msg.setReplyMarkup(keyboardMarkup);
                try {
                    sendMessage(msg); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }



        }

        System.out.println("control fin ejecucion metodo");

    }

    @Override
    public String getBotUsername() {
        return "Jason_Voorhees_bot";
    }

    @Override
    public String getBotToken() {
        return "1100803158:AAGwMd7dgCDjmQQ6rRjVW6o6MU11s7R2F8o";
    }
}
