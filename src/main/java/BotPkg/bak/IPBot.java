package BotPkg.bak;

import BotPkg.login.ServerStatusException;
import BotPkg.login.WrongParameterException;
import BotPkg.rootPkg.Utils;
import lombok.extern.java.Log;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static BotPkg.rootPkg.TgUserState.*;

/**
 * Created by OsipovMS on 27.04.2018.
 */
//@Deprecated
@Log
public class IPBot extends TelegramLongPollingBot {


    public Utils u;


    private final String YES = "YES";
    private final String NO = "NO";

//    private WebbReq webbReq = new WebbReq(false);
    //private Utils utils = Utils.INSTANCE;
    //todo = remake Singleton usage in thi Class


    IPBot(){
        u = Utils.INSTANCE;
        u.getRtData().init(u);
        u.setUseProx(false);
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
        if (messageText.equals("?")||messageText.equals("/help")){
            this.showAbout(chat_id);
        }
        // 1
        else if(messageText.equals("*")||messageText.equals("/start")){

            printRootMenu(chat_id);
        }
        // 2
        else {
            showAbout(chat_id);
//            this.printTgMessage(chat_id,"Ok");
        }
    }

    public void printRootMenu(Long chat_id){
        StringBuilder sb = new StringBuilder().append(" Список КАМАЗов (трэкеры)").append("\n").append("Выбор объекта:").append("\n");
        u.setTgUserSate(chat_id, ObjectSelectMenu);
        u.setSelectedObjectId(chat_id,null);
        this.buttonToSelectPrint(chat_id, sb.toString(), u.getRtData().getRootObjectList());

    }


    public void printRepeatButton(Long chat_id){
        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("repeat", " ДА ");
        stringMap.put("root", " НЕТ ");
        this.buttonToSelectPrint(chat_id, "Повторить ? ", stringMap/*u.getRtData().getRootObjectList()*/  );

    }



    public void printObjectMenu(Long chat_id, String callBackData){
        StringBuilder sb = new StringBuilder().append(" Просмотр параметров :").append("\n");
        u.setTgUserSate(chat_id, ParameterSelectMenu);

        this.buttonToSelectPrint(chat_id, sb.toString(), u.getRtData().getParamButtonMapToPrint() );

    }

    public void printTechParam(Long chatId, String objectId){
        printTgMessage(chatId, " Tех. параметры : " );
        String locSt = u.getRtData().getObjectData(objectId);
        printTgMessage(chatId, u.getRtData().getStringTechParam(locSt));

    }

    private void printGeoLocation (Utils u, Long chatId, String objectId) throws IOException, ServerStatusException {
        String locSt = u.getRtData().getObjectData(objectId);
        u.getBotMessaging().sendGeoLocation(u,chatId, locSt);

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

            String objectId = null;
            try {
                objectId = u.getSelectedObjectId(chat_id);
            } catch (NullPointerException e){
                log.info(e.getMessage());
            }


            if (u.getTgUserState(chat_id).equals(ObjectSelectMenu)) {

//                this.printTgMessage(chat_id, "ID = " + callBackData);

                u.setSelectedObjectId(chat_id,callBackData);

                this.printObjectMenu(chat_id,callBackData);

            }else {
                // объект выбран

                if (objectId==null){
                    u.setTgUserSate(chat_id,RootMenu);
                    throw new WrongParameterException("Menu position (of chatId) = laje");
                }



                if (callBackData.equals("geo")) {
                    u.setTgUserSate(chat_id,GeoPositionWatch);

//                    String locSt = u.getRtData().getObjectData(objectId);
//                    u.getBotMessaging().sendGeoLocation(u,chat_id, locSt);

                    printGeoLocation(u,chat_id,objectId);

                    printRepeatButton(chat_id);

                }

                if (callBackData.equals("tech")) {
                    u.setTgUserSate(chat_id,TechParamWatch);
//                    u.setSelectedObjectId();
//                    printTgMessage(chat_id,objectId + "  :  " + callBackData);

                    printTechParam(chat_id, objectId);

                    printRepeatButton(chat_id);

                }

                if (callBackData.equals("repeat")) {
                    if (u.getTgUserState(chat_id).equals(GeoPositionWatch)){
                        printGeoLocation(u,chat_id,objectId);
                    }
                    if (u.getTgUserState(chat_id).equals(TechParamWatch)){
                        printTechParam(chat_id, objectId);
                    }
                    printRepeatButton(chat_id);

                }

                if (callBackData.equals("root")) {
                    u.setTgUserSate(chat_id,RootMenu);
                    //this.printRootMenu(chat_id);
                    this.showAbout(chat_id);

                }



                System.err.println("DATA = !!!!!!!!!!!!!!! =  = " + u.getRtData().getObjectData(objectId));

            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     * no need = > // todo DELETE
     * @param chatId
     */
    private void showAbout(Long chatId){
        StringBuilder sb = new StringBuilder()
                .append("Список КАМАЗов с трекерами ").append("\n")
                .append("/help или ? - описание ").append("\n")
                .append("/start или * - начать просмотр ").append("\n");

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