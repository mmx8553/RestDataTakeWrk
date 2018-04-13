package EmptyBot;

import com.google.inject.Singleton;
import login.ConnExec;
import login.WebbReq;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by OsipovMS on 11.04.2018.
 */

@SuppressWarnings("unchecked")
@Singleton
public class  Utils {

    private static Utils instance = new Utils();
    private Utils(){
    }
    public static Utils getInstance(){
        return instance;
    }

    public WebbReq webbReq = new WebbReq(true);
    public ConnExec ce = new ConnExec();



    private String token;

    public String payload = "{\"login\":\"kamaz-maxim\",\"password\":\"nEWkj3_hG5\"}";
    public String jsonLoginPassword = "{\"login\":\"kamaz-maxim\",\"password\":\"nEWkj3_hG5\"}";
    public String baseaddr = "https://sandbox.rightech.io/api/v1";
    public String requestTokenUrl = "/auth/token";
    public String requestObjects = "/objects";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



//    public static final String objectsUrl = "";
//    private TgUserState tgUserState;

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
        ConnExec ce = new ConnExec();
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
     */
    public Map<String,String> getParamButtonMapToPrint(){

        Map <String,String> retMap =  new HashMap<>();

        retMap.put("geo","ГеоЛокация");
        retMap.put("tech","Тех.Данные");

        return retMap;
    }



    public Map<String,String> getBotRootObjectsMap(WebbReq webbReq, ConnExec ce){



        System.out.println();
        Map<String,String> stringMap = new HashMap<>();

        JSONArray result = null;
        try {
            result = webbReq.getWebb().get(ce.baseaddr+ce.requestObjects)
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


    public Map<String,String> getParamList(WebbReq webbReq, ConnExec ce){



        System.out.println();
        Map<String,String> stringMap = new HashMap<>();

        JSONArray result = null;
        try {
            result = webbReq.getWebb().get(ce.baseaddr+ce.requestObjects)
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
