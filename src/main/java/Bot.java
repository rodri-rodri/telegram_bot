import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private boolean busquedaAbierta = false; //BOOLEANO QUE INDICA SI ESTA ACTIVA LA OPCION DE BUSQUEDA
    private String busquedaUsuario=" ";
    private SendMessage message = new SendMessage();
    private Search search = new Search();
    private String[][] videoInfo= search.Busqueda(busquedaUsuario);

    @Override
    public void onUpdateReceived(Update update) {

        long chat_id = update.getMessage().getChatId(); //OBTENGO CHATID EN UNA VARIABLE

        System.out.println(update.getMessage().getFrom().getFirstName()+ " - " + update.getMessage().getText()); //MUESTRO EN CONSOLA NOMBRE Y MENSAJE RECIBIDO

        if (busquedaAbierta) { //SOLO ENTRA PARA LAS BUSQUEDAS DE YOUTUBE CUANDO busquedaAbierta ES TRUE....

            busquedaUsuario = update.getMessage().getText();

             //INICIALIZO CLASE SEARCH DE YOUTUBE

            videoInfo= search.Busqueda(busquedaUsuario); //ARRAY BIDIMENSIONAL QUE GUARDA INFO SOBRE ID DEL VIDEO[x][] Y NOMBRE[][x]

            message.setText("He encontrado estos videos...");

            message.setChatId(update.getMessage().getChatId());

            try {
                execute(message);
            } catch (TelegramApiException e) {

                System.out.println("ERROR: EL MENSAJE NO FUE ENVIADO");
            }

            message = new SendMessage().setChatId(chat_id).setText("Estos son los videos que tengo para ti!");

            //A PARTIR DE AQUI CREAMOS TECLADO TECLADO
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

            //EL TECLADO ES UN ARRAYLIST DE BOTONES

            List<KeyboardRow> keyboard = new ArrayList<>();

            KeyboardRow row = new KeyboardRow();

            row.add(videoInfo[0][1]);
            row.add(videoInfo[1][1]);
            row.add(videoInfo[2][1]);

            keyboard.add(row);

            row = new KeyboardRow();

            row.add(videoInfo[3][1]);
//            row.add(videoInfo[4][1]);
//            row.add(videoInfo[5][1]);
            // Add the second row to the keyboard
            row.add("VOLVER AL TECLADO");
            keyboard.add(row);
            // Set the keyboard to the markup
            keyboardMarkup.setKeyboard(keyboard);
            // Add it to the message
            message.setReplyMarkup(keyboardMarkup);
            try {
                sendMessage(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

//            for (int i=0;i<6;i++){
//
//                message.setText("https://www.youtube.com/watch?v="+videoInfo[i][0]);
//
//                message.setChatId(update.getMessage().getChatId());
//
//                try {
//                    execute(message);
//                } catch (TelegramApiException e) {
//
//                    System.out.println("ERROR: EL VIDEO NO SE HA ENVIADO");
//
//                }
//            }


            busquedaAbierta = false;


        }


        if (update.getMessage().getText().equals("/youtube")) {

            message.setText("Que quieres buscar?");

            message.setChatId(update.getMessage().getChatId());

            try {
                execute(message);
            } catch (TelegramApiException e) {

                System.out.println("NO SE HA ENVIADO EL MENSAJE");
            }

            busquedaAbierta = true ;

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

            message = new SendMessage().setChatId(chat_id).setText("Quieres ver otro?" +
                    "\nSeleccionalo en la lista.." +
                    "\n\nQuieres una nueva busqueda?" +
                    "\nDale a VOLVER AL TECLADO");

            try {
                sendMessage(message);
            } catch (TelegramApiException e) {

                System.out.println("ERROR: EL VIDEO NO SE HA ENVIADO");

            }

        } else if (update.getMessage().getText().equals(videoInfo[3][1])){

            message = new SendMessage().setChatId(chat_id).setText("https://www.youtube.com/watch?v="+videoInfo[3][0]);

            try {
                sendMessage(message);
            } catch (TelegramApiException e) {

                System.out.println("ERROR: EL VIDEO NO SE HA ENVIADO");

            }
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

    @Override
    public String getBotUsername() {
        return "Jason_Voorhees_bot";
    }

    @Override
    public String getBotToken() {
        return "1100803158:AAGwMd7dgCDjmQQ6rRjVW6o6MU11s7R2F8o";
    }
}
