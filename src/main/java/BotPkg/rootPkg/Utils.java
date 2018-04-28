package BotPkg.rootPkg;

import BotPkg.login.RtData;
import BotPkg.login.WebbReq;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by OsipovMS on 11.04.2018.
 */

//@SuppressWarnings("unchecked")
//@Singleton

@Log
public enum Utils {

    INSTANCE;
//    private static Utils instance = new Utils();

    @Getter
    private String baseaddrRt = "https://sandbox.rightech.io/api/v1";

    @Getter
    private String requestTokenUrlRt = "/auth/token";

    @Getter
    private String requestObjectsRt = "/objects";

    @Getter
    private ProxConn proxyConnection;



    @Getter @Setter
    private RtData rtData;

    @Getter @Setter
    private BotMessaging botMessaging;

    @Getter @Setter
    private String rtToken ;


    @Getter @Setter
    private boolean useProx = true;

    @Getter
    private String tokenTg;

    @Getter
    private String nameTg;

    @Getter
    private String jsonRtLoginPassword;

    @Getter @Setter
    private int maxMessageOffset = 0;





    private Map<String,String> loginDataMap= new HashMap<String, String>();

    private YmlRead yml = new YmlRead(null);

    Utils(){
        this.init();
    }

    public Utils getInstance(){
        return INSTANCE;
    }

    public WebbReq webbRequest = new WebbReq(true);
    public RtData mxRequest = new RtData();



    private void init(){
        try {

            this.rtData = new RtData();
            this.botMessaging = new BotMessaging();
            this.proxyConnection = new ProxConn();
            loginDataMap = yml.getValues();

            tokenTg = loginDataMap.get("bot.token");

            nameTg = loginDataMap.get("bot.botname");

            JSONObject joRT = new JSONObject()
                    .put("login",loginDataMap.get("rt.login"))
                    .put("password",loginDataMap.get("rt.password"));
            jsonRtLoginPassword = joRT.toString();

            String stProxy= loginDataMap.get("bot.useproxy");
            useProx = (stProxy.equals("ok"));



        }catch (Exception e){
            System.err.println("Utils.init = error");
            e.printStackTrace();
        }
//        log.info("Utils.init = ok");
//        this LOG is not works in SINLETONE

    }


//    private String token;
//    public String getRtToken() {
//        return token;
//    }


    /**
     * текущее аоложение в дереве меню пользователя
     * K: chatId = Long
     * V: TgUserStatus = Enum_TYPE
     */
    private Map < Long, TgUserState> chatState  = new HashMap<>();

    /**
     * CALLBACK DATA selected in last menu
     */
    private Map < Long , String> chatCallbackData = new HashMap<>();


    public String getSelectedObjectId(Long chatId) {
        return chatCallbackData.get(chatId);
    }

    public void setSelectedObjectId(Long chatId , String data ) {
        chatCallbackData.put(chatId,data );
    }

    public void setTgUserSate(Long chatId, TgUserState state){
        chatState.put(chatId, state);

    }
    public TgUserState getTgUserState(Long chatId){
        return chatState.get(chatId);
    }



    public Map<String,String> getButtonMapToPrint(){
        RtData ce = new RtData();
        WebbReq webbReq = new WebbReq(false);

        if (!webbReq.createToken(webbReq)) {
            System.out.println("main = token problem");
        }

        Map <String,String> retMap =  getBotRootObjectsMap(webbReq,ce);

        return retMap;

    }



    /**
     * меню на объект
     * @return
     * <колбэк-дата, Надпись-на-кнопке>
     */
    public Map<String,String> getParamButtonMapToPrint(){

        Map <String,String> retMap =  new HashMap<>();

        retMap.put("geo","ГеоЛокация");
        retMap.put("tech","Тех.Данные");

        return retMap;
    }


@Deprecated
    public Map<String,String> getBotRootObjectsMap(WebbReq webbReq, RtData ce){


        log.info("aaa");

        //System.out.println();
        Map<String,String> stringMap = new HashMap<>();

        JSONArray result = null;
        try {
            result = webbReq.getWebb().get(ce.getRtBaseaddr()+ce.requestObjects)
                    .header("Authorization",new StringBuilder().append("Bearer ").append(webbReq.getToken()))
                    .retry(1, false) // at most one retry, don't do exponential backoff
                    .asJsonArray()
                    .getBody();
            //System.err.println("objects = " + result.toString());

        }catch (Exception e){

            System.out.println("Error - WebbReq = getObjectsList error =  :"+ e.getMessage());
            e.printStackTrace();

        }

        try {
            Iterator iterator = result.iterator();
            String itemName;
            String itemId;
            JSONObject jsonObject;
            while (iterator.hasNext()) {
                jsonObject = new JSONObject(iterator.next().toString());

                itemName = (String) jsonObject.get("name");
                itemId = (String) jsonObject.get("_id");
                //System.out.println(itemName);
                stringMap.put(itemId,itemName);
            }

        }catch (Exception e){
            System.out.println("Esc - 1get2Up3List = " + e.getMessage());
        }

        return stringMap;

    }


    public Map<String,String> getParamList(WebbReq webbReq, RtData ce){



        System.out.println();
        Map<String,String> stringMap = new HashMap<>();

        JSONArray result = null;
        try {
            result = webbReq.getWebb().get(ce.getRtBaseaddr()+ce.requestObjects)
                    .header("Authorization",new StringBuilder().append("Bearer ").append(webbReq.getToken()))
                    .retry(1, false) // at most one retry, don't do exponential backoff
                    .asJsonArray()
                    .getBody();
            //System.err.println("objects = " + result.toString());

        }catch (Exception e){
            System.out.println("Error - WebbReq = getObjectsList error =  :"+ e.getMessage());
            e.printStackTrace();

        }

        try {
            Iterator iterator = result.iterator();
            String itemName;
            String itemId;
            JSONObject jsonObject;
            while (iterator.hasNext()) {
                jsonObject = new JSONObject(iterator.next().toString());

                itemName = (String) jsonObject.get("name");
                itemId = (String) jsonObject.get("_id");
                //System.out.println(itemName);
                stringMap.put(itemId,itemName);
            }

        }catch (Exception e){
            System.out.println("Esc - 1get2Up3List = " + e.getMessage());
        }

        return stringMap;

    }

}
