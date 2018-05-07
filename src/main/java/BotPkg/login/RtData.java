package BotPkg.login;


import BotPkg.rootPkg.Utils;
import BotPkg.webbPkg.WebbException;
import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;


/**
 * Created by OsipovMS on 21.03.2018.
 */
@Log
public class RtData {

//    @Deprecated
//    public static final boolean useProxy = false; //true;

    public RtData() {
    }

    public WebbReq wr;

//    @Getter @Setter
//    private String jsonRtLoginPassword;


    @Getter
    private String rtToken;

    @Getter
    private String jsonLoginPassword ;

//    @Getter
//    private String rtBaseaddr ;


    public void init(Utils uu){
        this.jsonLoginPassword = uu.getJsonRtLoginPassword();
        this.rtBaseaddr = uu.getBaseaddrRt();
        this.tokenRtRequestNStore();
        wr = new WebbReq();
    }

//    public void init(Utils uu){
//        this.jsonLoginPassword = uu.getJsonRtLoginPassword();
//        this.rtBaseaddr = uu.getBaseaddrRt();
//
//        uu.setRtToken(this.tokenRtRequestNStore());
//    }




    public String requestTokenUrl = "/auth/token";
    public String requestObjects = "/objects";

    @Getter
    public static String  rtBaseaddr = "https://sandbox.rightech.io/api/v1";


    public String tokenRtRequestNStore() {
        String tmpStr = "";
        String tmpToken = "";

        try {
            tmpStr = sendRtRequest("POST", rtBaseaddr, requestTokenUrl, jsonLoginPassword, null).toString();
            tmpToken = new JSONObject(tmpStr).get("token").toString();
            System.err.println("RT Token = ok? answer = " + tmpStr);
        } catch (Exception e) {

            log.info(e.getMessage());
        }

        this.rtToken = tmpToken;
        return tmpToken;
    }



    public  Map<String,String> getRootObjectList(){

//        ArrayList<String> ast = new ArrayList<String>();
        Map<String,String> stringMap = new HashMap<>();

        try {


            String resultObjects = null;
            resultObjects =  sendRtRequest("GET", rtBaseaddr, "/objects", null, this.rtToken).toString();

            JSONArray JsnArr= new JSONArray(resultObjects);

            Iterator iterator = JsnArr.iterator();
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

        } catch (ParseException e) {
            log.info(e.getMessage());
        }
        catch (Exception e){
            log.info(e.getMessage());
        }
        return stringMap ;
    }



    /**
     * меню на объект
     * @return
     * <колбэк-дата, Надпись-на-кнопке>
     */
    public Map<String,String> getParamButtonMapToPrint(){

        Map<String,String> retMap =  new HashMap<>();

        retMap.put("geo","ГеоЛокация");
        retMap.put("tech","Тех.Данные");

        return retMap;
    }


    public String getObjectData(  String id) throws WebbException {

        JSONObject result = null;
        //String  result = null;
        String url = rtBaseaddr + requestObjects+"/"+id;

        String resultObjects = null;
        JSONArray JsnArr;

        try {

            resultObjects =  sendRtRequest("GET", url, "", null, this.rtToken).toString();
//            JsnArr= new JSONArray(resultObjects);

        }catch (WebbException we){
            log.log(Level.SEVERE,we.getMessage());
        }catch (Exception e) {
            log.log(Level.SEVERE,e.getMessage());
        }


        return resultObjects;
        //result.toString();
    }






    public  String sendRtRequest(String method, String baseAddr, String appendAddr, String payload, String authToken) {

        String requestAddres = new StringBuilder().append(baseAddr).append(appendAddr).toString();

        StringBuffer jsonString = new StringBuffer();


        try {

            URL url = new URL(requestAddres);
            HttpURLConnection connection;

            // RighTech - без прокси
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(method);  /* GET , POST */
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (authToken != null) {
                connection.setRequestProperty("Authorization", new StringBuilder()
                        .append("Bearer")
                        .append(" ")
                        .append(authToken)
                        .toString());
            }

            if (payload != null) {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(payload);
                writer.close();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //System.out.println(connection.getResponseCode());

            if (connection.getResponseCode() != 200) {
                log.info(connection.getResponseMessage());
                throw new ServerStatusException(connection.getResponseMessage());
            }

            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();

        } catch (ServerStatusException se) {
            //todo плохая авторизация переспросить токен
            log.info(se.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {

        }
        //return null;
        return jsonString.toString();
    }


    public String getUrlLocation(Long chatId, String locationParam){

        String lon = "0";
        String lat= "51";
        String state = "";
        Utils uu = Utils.INSTANCE;

        try {
            JSONObject jo = new JSONObject(locationParam);
            state = jo.get("state").toString();
            lat = new JSONObject(state).get("lat").toString();
            lon = new JSONObject(state).get("lon").toString();


        }catch (JSONException e){
            log.info(e.getMessage());
        }



        String stUrl = new StringBuilder().append("https://api.telegram.org/bot")
                .append(uu.getTokenTg())
                .append("/sendLocation?chat_id=")
                .append(chatId)
                .append("&latitude=")
                .append(lat)
                .append("&longitude=")
                .append(lon).toString();

        return stUrl;
    }


    public String getStringParamFromJSON(String jsonString, String param){

        try {
            JSONObject jo = new JSONObject(jsonString);
            String state = jo.get("state").toString();
            String result = new JSONObject(state).get(param).toString();
            return result;

        }catch (JSONException e){
            log.info(e.getMessage());
        }

        return "no_data";
    }


    public String getStringTechParam( String jsonParamString){

//        mmx

        String speed= "0";
        String fuel = "0";
        String fuelTemp = "0";

        String state = "";
        Utils uu = Utils.INSTANCE;

        speed = getStringParamFromJSON(jsonParamString,"speed");

        fuel = getStringParamFromJSON(jsonParamString,"fuel-level");
        if (fuel.equals("no_data")){
            fuel = getStringParamFromJSON(jsonParamString,"fuel_lev_p");
        }


        String textParamOutput = new StringBuilder().append("\n")
                .append("1. скорость : ").append(speed).append("\n")
                .append("2. топливо : ").append(fuel).append("\n")
                .append("etc.").toString();

        return textParamOutput;
    }









//    public  ArrayList<String> getUpList(){
////        RtData ce = new RtData();
////        System.out.println();
//        ArrayList<String> ast = new ArrayList<String>();
//        try {
//
//
//            String resultObjects;
//            resultObjects =  RtData.sendRequest("GET",this.getRtBaseaddr(), "/objects", null, this.rtToken).toString();
//
//            JSONArray JsnArr= new JSONArray(resultObjects);
//
//            Iterator iterator = JsnArr.iterator();
//            String itemName;
//            while (iterator.hasNext()) {
//                itemName = (String) new JSONObject(iterator.next().toString()).get("name");
//                System.out.println(itemName);
//                ast.add(itemName);
//            }
//
//        } catch (ParseException e) {
//            log.info(e.getMessage());
////            System.out.println("parse Exc = " + e.getMessage());
//            //e.printStackTrace();
//        }
//        catch (Exception e){
//            log.info(e.getMessage());
////            System.out.println("Exc = " + e.getMessage());
//        }
//
//        return ast;
//
//    }




//    public static String sendRequest(String method, String baseAddr, String appendAddr, String payload, String authToken) {
//
//        String requestAddres = new StringBuilder().append(baseAddr).append(appendAddr).toString();
//
//        StringBuffer jsonString = new StringBuffer();
////        String proxx = "178.238.228.187:9090";
////
////        String[] tok = proxx.split(":", 2);
////        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(tok[0], new Integer(tok[1]).intValue()));
//
//        Proxy proxy = null; // RighTech - без прокси
//
//
//        try {
//
//            URL url = new URL(requestAddres);
//            HttpURLConnection connection;
////            connection = (HttpURLConnection) url.openConnection(proxy);
//
//            // RighTech - без прокси
//            connection = (HttpURLConnection) url.openConnection();
//
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setRequestMethod(method);  /* GET , POST */
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//
//            if (authToken != null) {
//                connection.setRequestProperty("Authorization", new StringBuilder()
//                        .append("Bearer")
//                        .append(" ")
//                        .append(authToken)
//                        .toString());
//            }
//
//            if (payload != null) {
//                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
//                writer.write(payload);
//                writer.close();
//            }
//            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            //System.out.println(connection.getResponseCode());
//
//            if (connection.getResponseCode() != 200) {
//                log.info(connection.getResponseMessage());
//                throw new ServerStatusException(connection.getResponseMessage());
//            }
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                jsonString.append(line);
//            }
//            br.close();
//            connection.disconnect();
//
//        } catch (ServerStatusException se) {
//            log.info(se.getMessage());
//        } catch (Exception e) {
//            log.info(e.getMessage());
//        } finally {
//
//        }
//        //return null;
//        return jsonString.toString();
//    }




//    @Deprecated
//    public String getUpdates() {
//
//        String endOfAddr = Utils.INSTANCE.getTokenTg() + "/getUpdates?offset=0";
//        return this.sendRequest("GET", "https://api.telegram.org/bot", endOfAddr, "", "");
//
//    }




}