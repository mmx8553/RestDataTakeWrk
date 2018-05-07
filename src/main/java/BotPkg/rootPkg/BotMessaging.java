package BotPkg.rootPkg;

import BotPkg.login.ServerStatusException;
import lombok.extern.java.Log;

import java.io.IOException;

/**
 * Created by OsipovMS on 27.04.2018.
 */
@Log
public class BotMessaging {

    public BotMessaging(){

    }


    Utils u  = Utils.INSTANCE;

    public String getSendingTextMessageUrl(Long chatId, String message){

        Utils u  = Utils.INSTANCE;

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


//    public void sendMessage(Long chatId, String message) throws IOException, ServerStatusException {
//        u.getProxyConnection().getBotSendtext(u.getBotMessaging().getSendingTextMessageUrl(chatId, message));
//
//    }

    public void sendMessage(Long chatId, String message) throws IOException, ServerStatusException {
        Utils u = Utils.INSTANCE;
        u.getProxyConnection().getReplyOfBotSendtextUrl(u.getBotMessaging().getSendingTextMessageUrl(chatId, message));

    }

//    public String getUrlLocation(Long chatId, String locationParam){
//
//        String lon = "0";
//        String lat= "51";
//        String state = "";
//        Utils uu = Utils.INSTANCE;
//
//        try {
//            JSONObject jo = new JSONObject(locationParam);
//            state = jo.get("state").toString();
//            lat = new JSONObject(state).get("lat").toString();
//            lon = new JSONObject(state).get("lon").toString();
//
//
//        }catch (JSONException e){
//            log.info(e.getMessage());
//        }
//
//
//
//        String stUrl = new StringBuilder().append("https://api.telegram.org/bot")
//                .append(uu.getTokenTg())
//                .append("/sendLocation?chat_id=")
//                .append(chatId)
//                .append("&latitude=")
//                .append(lat)
//                .append("&longitude=")
//                .append(lon).toString();
//
//        return stUrl;
//    }
//

    public void sendGeoLocation(Utils uu, Long chatId, String param) throws IOException, ServerStatusException {
        uu.getProxyConnection().getReplyOfBotSendtextUrl( uu.getRtData().getUrlLocation(chatId, param));
    }





    public void processAfterGotTextMessage(Long chatID , String gotMsg) throws IOException, ServerStatusException {
        Utils u = Utils.INSTANCE;
        if (gotMsg.equals("?")){

            u.getBotMessaging().sendMessage(chatID,"some helping text");
        }

    }

//    public void processAfterGotTextMessage(Long chatID , String gotMsg) throws IOException, ServerStatusException {
//        if (gotMsg.equals("?")){
//
//            u.getBotMessaging().sendMessage(chatID,"some helping text");
//        }
//
//    }

    public void processAfterGotCallback(Long chatID , String gotData){
        Utils u  = Utils.INSTANCE;


    }




}
