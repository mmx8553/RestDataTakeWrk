package EmptyBot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by OsipovMS on 02.04.2018.
 */
public class IPBot extends TelegramLongPollingBot {

    private final String YES = "YES";
    private final String NO = "NO";

    IPBot(DefaultBotOptions defaultBotOptions){
        super(defaultBotOptions);
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

            sb.append("KAMAZ truck selection").append("\n");
            sb.append("Please, type ? : ");

            System.out.println("msg - call bk");



            if (callBackData == "") {

//            }else if(kmzData.isModelParametr(callBackData)) {
//                sb = new StringBuilder().append("Is this OK ? ").append("\n");
//
//                buttonToSelectPrint(chat_id, sb.toString(),yesNo);
//
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

            }

            /**
             * начать подбор - сначала тип
             */
            else if(messageText.equals("*")||messageText.equals("/start")){

                StringBuilder sb = new StringBuilder().append("Selection of KAMAZ").append("\n");
                sb.append("Type ?  : ");


            } else {
                           }
        }
    }


    private void showNotSort (Long chatId){
        StringBuilder sb = new StringBuilder()
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


    @Override
    public String getBotUsername() {
        return "Kamazi_bot";//"KamazBot"; //"Kamaz_bot";
    }

    @Override
    public String getBotToken() {
        return "523022396:AAFryNlfFVL5WOTAD35BM4bBL832zis_ERE";
    }

}
