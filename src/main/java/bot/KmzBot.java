package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmx on 06.02.2018.
 */
public class KmzBot extends TelegramLongPollingBot {


//    public KmzBot(KmzData kmzData) {
//        this.kmzData = kmzData;
//    }

    public KmzBot(DefaultBotOptions options) {
        super(options);
    }

    private KmzData kmzData; // = new KmzData();
//    private final String YES = "ДА";
//    private final String NO = "НЕТ";
    private final String YES = "YES";
    private final String NO = "NO";


    private List<List<InlineKeyboardButton>> getButtonList(String[] arrayNames) {

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline; // = new ArrayList<>();

            for (String str : arrayNames) {
                rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText(str).setCallbackData(str));
                System.out.println("row = " + rowInline.get(0).toString());

                rowsInline.add(rowInline);
            }

        System.out.println("btn forming = ok");

        return rowsInline;
    }




    private void buttonToSelectPrint(Long chatId, String msg, String[] captions) {
        SendMessage message = new SendMessage().setChatId(chatId).setText(msg);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        //отрисовать кнопки
        markupInline.setKeyboard(getButtonList(captions));
        message.setReplyMarkup(markupInline);

        try {
            execute(message); //(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

//        return message;
    }


    private void showHelp(Long chatId) {
//        StringBuilder sb = new StringBuilder().append("Подбор КАМАЗа").append("\n");
//        sb.append("Тип, Модель, ... ");
        StringBuilder sb = new StringBuilder().append("KAMAZ truck selestion").append("\n");
        sb.append("Type, Model, ... etc. ");

        this.printMessage(chatId,sb.toString());

    }

    private void showNotSort (Long chatId){
        StringBuilder sb = new StringBuilder()
//                .append("КАМАЗ персонально для Вас!").append("\n")
//                .append("? - описание ").append("\n")
//                .append("* - начать выбор ").append("\n");
                .append("KAMAZ just for You!").append("\n")
                .append("? - description ").append("\n")
                .append("* - lets start selection ").append("\n");

        this.printMessage(chatId,sb.toString());

    }

    private void printMessage(Long chatId, String msg){
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    private void showPic (Long chatId, String model){
        InputStream is = null;

        StringBuilder sb = new StringBuilder().append("d://001//").append(kmzData.getPicAddrByModel(model)).append(".jpg");
        System.out.println(sb.toString());
        try {
            is = new FileInputStream(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }

        SendPhoto msg = new SendPhoto()
                .setChatId(chatId).setNewPhoto("kamaz",is)
                .setCaption("Photo");
        try {
            sendPhoto(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        try {
            is.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("update recieved");

        if (update.hasCallbackQuery()){

            //String answerText = "";
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            StringBuilder sb = new StringBuilder();
            String callBackData = update.getCallbackQuery().getData();

            SendMessage message;

//            sb.append("Подбор КАМАЗа").append("\n");
//            sb.append("Выбирайте тип: ");
            sb.append("KAMAZ truck selection").append("\n");
            sb.append("Please, type ? : ");

//            System.out.println("is type parametr");
            System.out.println("msg - call bk");



            if (kmzData.isTypeParametr(callBackData)) {

//                sb = new StringBuilder().append("Выбирайте модель: ").append("\n");
                sb = new StringBuilder().append("Please, choose truck model: ").append("\n");
                buttonToSelectPrint(chat_id, sb.toString(),kmzData.getModelList(callBackData));

            }else if(kmzData.isModelParametr(callBackData)) {
                this.showPic(chat_id,callBackData);
//                sb = new StringBuilder().append("Это подойдет ? ").append("\n");
                sb = new StringBuilder().append("Is this OK ? ").append("\n");
                //this.printMessage(chat_id,sb.toString());
                String[] yesNo = {YES,NO};
                buttonToSelectPrint(chat_id, sb.toString(),yesNo);

            } else if (callBackData.equals(YES)) {

//                sb = new StringBuilder().append("Поздравляю!").append("\n")
                sb = new StringBuilder().append("Great!").append("\n")
//                        .append("Хороший выбор!").append("\n");
                        .append("Good choice!").append("\n");
//                        .append(update.getCallbackQuery().getMessage().getContact().getPhoneNumber());

                this.printMessage(chat_id,sb.toString());

            } else if (callBackData.equals(NO)) {
                sb = new StringBuilder().append("OK!").append("\n")
//                        .append("Выберем опять!").append("\n");
                        .append("Let's choose again!").append("\n");


                this.printMessage(chat_id,sb.toString());
                showNotSort(chat_id);

            } else {

                //showHelp(chat_id);
                //todo
            }

        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            System.out.println("update message happens");
            String messageText = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            String answerText;
            System.err.println(messageText.toString());
            if (messageText.equals("?")){
                showHelp(chat_id);
            }

            /**
             * начать подбор - сначала тип
             */
            else if(messageText.equals("*")||messageText.equals("/start")){

//                StringBuilder sb = new StringBuilder().append("Подбор КАМАЗа").append("\n");
//                sb.append("Выбирайте тип: ");
                StringBuilder sb = new StringBuilder().append("Selection of KAMAZ").append("\n");
                sb.append("Type ?  : ");

                buttonToSelectPrint(chat_id, sb.toString(),kmzData.getTypes());


/*
                SendMessage message = new SendMessage().setChatId(chat_id).setText("tel ?");

                ReplyKeyboardMarkup  mrkKbd = new ReplyKeyboardMarkup ();
                message.setReplyMarkup(mrkKbd);
                //отрисовать кнопки
                mrkKbd.setSelective(true);//  setKeyboard(getButtonList(new String[] {"тел"}));
                mrkKbd.setResizeKeyboard(true);
                mrkKbd.setOneTimeKeyboard(true);

                List<KeyboardRow> keyboard = new ArrayList<>();

                // first keyboard line
                KeyboardRow keyboardFirstRow = new KeyboardRow();
                KeyboardButton keyboardButton = new KeyboardButton();
                keyboardButton.setText("Share your number >").setRequestContact(true);
                keyboardFirstRow.add(keyboardButton);

                keyboard.add(keyboardFirstRow);

                // add list to our keyboard
                mrkKbd.setKeyboard(keyboard);


                try {
                    execute(message); //(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                System.out.println("#############");
                System.out.println(update.getMessage().getContact().getPhoneNumber());
                System.out.println("#############");


*/



            } else {
                showNotSort(chat_id);            }
        }
    }



    @Override
    public String getBotUsername() {
        return "Kamazi_bot";//"KamazBot"; //"Kamaz_bot";
    }

    @Override
    public String getBotToken() {
        return "523022396:AAFryNlfFVL5WOTAD35BM4bBL832zis_ERE";
    }
}
