package BotPkg.bak;

import BotPkg.rootPkg.Utils;
import BotPkg.login.WebbReq;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static BotPkg.rootPkg.TgUserState.*;

/**
 * Created by OsipovMS on 27.04.2018.
 */
@Deprecated
public class IPBot extends TelegramLongPollingBot {


    Utils u = Utils.INSTANCE;


    private final String YES = "YES";
    private final String NO = "NO";

    private WebbReq webbReq = new WebbReq(false);
    private Utils utils = Utils.INSTANCE;
    //todo = remake Singleton usage in thi Class

    IPBot(){
        //System.out.println(getBotToken());

    }




    private  List<List<InlineKeyboardButton>> getButtonList(Map<String,String> arrayNames) {

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline; // = new ArrayList<>();

        try{
            if (arrayNames instanceof Map) {
                int i = 0;
                for ( Map.Entry<String,String> pair : arrayNames.entrySet()) {
                    rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText(pair.getValue()).setCallbackData(pair.getKey()));
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


    private void  buttonToSelectPrint  (Long chatId, String msg, Map<String,String> captions) {
        SendMessage message = new SendMessage().setChatId(chatId).setText(msg);


        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        //отрисовать кнопки
        markupInline.setKeyboard(getButtonList(captions));
        //ReplyKeyboard
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
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            processTextUpdate(update);
        }
    }



    public void processTextUpdate (Update update){
        System.out.println("update message happens");
        String messageText = update.getMessage().getText();
        long chat_id = update.getMessage().getChatId();
        //~LOG
        System.err.println(messageText.toString());
        System.out.println("");

        // 0
        if (messageText.equals("?")){
            this.showAbout(chat_id);
        }
        // 1
        else if(messageText.equals("*")||messageText.equals("/start")){

            printRootMenu(chat_id);
        }
        // 2
        else {
            this.printTgMessage(chat_id,"OK");
            //                update.getMessage().
        }
    }

    public void printRootMenu(Long chat_id){
        StringBuilder sb = new StringBuilder().append(" KAMAZ - трэкеры").append("\n").append("Выбор объекта:").append("\n");
        utils.setTgUserSate(chat_id, ObjectSelectMenu);
        this.buttonToSelectPrint(chat_id, sb.toString(), utils.getButtonMapToPrint());

    }

    public void printObjectMenu(Long chat_id, String callBackData){
        StringBuilder sb = new StringBuilder().append(" Просмотр параметров :").append("\n");
        utils.setTgUserSate(chat_id, ParameterSelectMenu);

        this.buttonToSelectPrint(chat_id, sb.toString(), utils.getParamButtonMapToPrint() );

    }




    public void processCallbackQuery(Update update){

        long chat_id = update.getCallbackQuery().getMessage().getChatId();

        String strClBkData = update.getCallbackQuery().getData().toString();

        //        StringBuilder sb = new StringBuilder();
        String callBackData = update.getCallbackQuery().getData();

        SendMessage message;

        //        sb.append("KAMAZ truck ...").append("\n");

        System.err.println("msg - call bk = " + strClBkData);


        /**
         * response to user depends on  UserMenuPosition  <=
         */


        /**
         * SECTION OF MENU - SELECT OBJECT TO USE
         */
        try {

            if (utils.getTgUserState(chat_id).equals(ObjectSelectMenu)) {

                this.printTgMessage(chat_id, "ID = " + callBackData);

                utils.setSelectedObjectId(chat_id,callBackData);

                this.printObjectMenu(chat_id,callBackData);

            }else if (utils.getTgUserState(chat_id).equals(ParameterSelectMenu)){
                String objectId = utils.getSelectedObjectId(chat_id);
                printTgMessage(chat_id,objectId + "  :  " + callBackData);

                String locSt = "";

                System.out.println(webbReq.getObjectGeoData(webbReq,objectId));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            //printRootMenu(chat_id);
        }


        //
        //        if (callBackData.equals("")) {
        //            //in this case = do nothing
        //        } else if (callBackData.equals("123")){
        //                printTgMessage(chat_id,"Оч. секретный раздел !");
        //        }else {
        //            //showHelp(chat_id);     //todo
        //        }
    }


    /**
     * no need = > // todo DELETE
     * @param chatId
     */
    private void showAbout(Long chatId){
        StringBuilder sb = new StringBuilder()
                .append("List of KAMAZ robocars ").append("\n")
                .append("? - description ").append("\n")
                .append("* - do smth else ").append("\n");

        this.printTgMessage(chatId,sb.toString());

    }

    private void printTgMessage(Long chatId, String msg){
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
    public String getBotUsername()
    {
        return u.getNameTg();
    }

    @Override
    public String getBotToken() {
        return u.getTokenTg();
    }


}