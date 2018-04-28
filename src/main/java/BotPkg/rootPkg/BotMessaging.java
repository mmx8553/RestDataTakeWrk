package BotPkg.rootPkg;

import BotPkg.login.ServerStatusException;

import java.io.IOException;

/**
 * Created by OsipovMS on 27.04.2018.
 */
public class BotMessaging {

    public BotMessaging(){

    }


    Utils u  = Utils.INSTANCE;

    public String getSendingTextMessageUrl(Long chatId, String message){

        StringBuilder sb = new StringBuilder();
        String methodMsgChat = "/sendMessage?chat_id=";
        String methodMsgTxt = "&text=";

        sb.append("https://api.telegram.org/bot")
                .append(u.getTokenTg())
                .append(methodMsgChat)
                .append(chatId.toString())
                .append(methodMsgTxt)
                .append(message);

        String requestUrl= sb.toString();

        return requestUrl;
    }


    public void sendMessage(Long chatId, String message) throws IOException, ServerStatusException {
        u.getProxyConnection().getBotSendtext(u.getBotMessaging().getSendingTextMessageUrl(chatId, message));

    }



    public void processAfterGotTextMessage(Long chatID , String gotMsg) throws IOException, ServerStatusException {
        if (gotMsg.equals("?")){

            u.getBotMessaging().sendMessage(chatID,"some helping text");
        }

    }

    public void processAfterGotCallback(Long chatID , String gotData){

    }




}
