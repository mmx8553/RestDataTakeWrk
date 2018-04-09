package EmptyBot;

import login.WebbReq;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OsipovMS on 02.04.2018.
 */
public class IPBot extends TelegramLongPollingBot {

    private final String YES = "YES";
    private final String NO = "NO";

    private WebbReq webbReq = new WebbReq();
    IPBot(/*DefaultBotOptions defaultBotOptions*/){
    //    super(defaultBotOptions);
    }




    private <T> List<List<InlineKeyboardButton>> getButtonList(T /*String[]*/ arrayNames) {


        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline; // = new ArrayList<>();

        try{
            if (arrayNames instanceof String[]) {
                for (String str : (String[]) arrayNames) {
                    rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText(str).setCallbackData(str));
                    System.out.println("row = " + rowInline.get(0).toString());
                    rowsInline.add(rowInline);
                }
            }
        }catch (ClassCastException cce){
            System.err.println(cce.getMessage());
        }


        System.out.println("btn forming = ok");

        return rowsInline;
    }



    private <T> void  buttonToSelectPrint  (Long chatId, String msg, /*String[]*/ T captions) {
        SendMessage message = new SendMessage().setChatId(chatId).setText(msg);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        //отрисовать кнопки
        markupInline.setKeyboard(getButtonList(captions));

        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("update recieved");

        if (update.hasCallbackQuery()){

            processCallbackQuery(update);

            //String answerText = "";
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            //update.getCallbackQuery().getData();

            StringBuilder sb = new StringBuilder();
            String callBackData = update.getCallbackQuery().getData();

            SendMessage message;

            sb.append("KAMAZ truck selection").append("\n");
            sb.append("Please, type ? : ");

            System.out.println("msg - call bk");



            if (callBackData == "") {


            } else if (callBackData.equals(YES)) {

                sb = new StringBuilder().append("Cool!").append("\n");

                this.printMessage(chat_id,sb.toString());

            } else if (callBackData.equals(NO)) {
                sb = new StringBuilder().append("OK!").append("\n")
                        .append("Do it again!").append("\n");

                this.printMessage(chat_id,sb.toString());
                showNotSort(chat_id);

            } else {

                //showHelp(chat_id);
                //todo

            }

        }

        if (update.hasMessage() && update.getMessage().hasText()) {

            // Set variables

            processTextUpdate(update);

                           }
        }



    public void processTextUpdate (Update update){
        System.out.println("update message happens");
        String messageText = update.getMessage().getText();
        long chat_id = update.getMessage().getChatId();

//        String answerText;

        System.err.println(messageText.toString());

        if (messageText.equals("?")){

        }
        else if(messageText.equals("*")||messageText.equals("/start")){

            StringBuilder sb = new StringBuilder().append(" KAMAZ - blah blah blah").append("\n");
            this.printMessage(chat_id,sb.toString());
            String[] st  = webbReq.getButtonListToPrint();
            this.buttonToSelectPrint(chat_id,"aaa", st);
        } else {
                this.printMessage(chat_id,"OK");

        }
    }


    public void processCallbackQuery(Update update){

    }


    private void showNotSort (Long chatId){
        StringBuilder sb = new StringBuilder()
                .append("KAMAZ list").append("\n")
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


    @Override
    public String getBotUsername() {
        return "Kamazi_bot";//"KamazBot"; //"Kamaz_bot";
    }

    @Override
    public String getBotToken() {
        return "523022396:AAFryNlfFVL5WOTAD35BM4bBL832zis_ERE";
    }

}
