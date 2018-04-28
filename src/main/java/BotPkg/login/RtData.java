package BotPkg.login;


import BotPkg.rootPkg.Utils;
import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by OsipovMS on 21.03.2018.
 */
@Log
public class RtData {

//    @Deprecated
//    public static final boolean useProxy = false; //true;

    public RtData() {

    }


    @Getter
    private String rtToken;

    @Getter
    private String jsonLoginPassword ;

    @Getter
    private String rtBaseaddr ;

    public void init(Utils uu){
        this.jsonLoginPassword = uu.getJsonRtLoginPassword();
        this.rtBaseaddr = uu.getBaseaddrRt();

        uu.setRtToken(this.tokenRtRequestNStore());
    }




    public String requestTokenUrl = "/auth/token";
    public String requestObjects = "/objects";



    public String tokenRtRequestNStore() {
        String tmpStr = "";
        String tmpToken = "";

        try {
            tmpStr = RtData.sendRequest("POST", rtBaseaddr, requestTokenUrl, jsonLoginPassword, null).toString();
            tmpToken = new JSONObject(tmpStr).get("token").toString();
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        this.rtToken = tmpToken;

        return tmpToken;

    }


    public  ArrayList<String> getUpList(){
//        RtData ce = new RtData();
//        System.out.println();
        ArrayList<String> ast = new ArrayList<String>();
        try {


            String resultObjects;
            resultObjects =  RtData.sendRequest("GET",this.getRtBaseaddr(), "/objects", null, this.rtToken).toString();

            JSONArray JsnArr= new JSONArray(resultObjects);

            Iterator iterator = JsnArr.iterator();
            String itemName;
            while (iterator.hasNext()) {
                itemName = (String) new JSONObject(iterator.next().toString()).get("name");
                System.out.println(itemName);
                ast.add(itemName);
            }

        } catch (ParseException e) {
            log.info(e.getMessage());
//            System.out.println("parse Exc = " + e.getMessage());
            //e.printStackTrace();
        }
        catch (Exception e){
            log.info(e.getMessage());
//            System.out.println("Exc = " + e.getMessage());
        }

        return ast;

    }




    public static String sendRequest(String method, String baseAddr, String appendAddr, String payload, String authToken) {

        String requestAddres = new StringBuilder().append(baseAddr).append(appendAddr).toString();

        StringBuffer jsonString = new StringBuffer();
//        String proxx = "178.238.228.187:9090";
//
//        String[] tok = proxx.split(":", 2);
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(tok[0], new Integer(tok[1]).intValue()));

        Proxy proxy = null; // RighTech - без прокси


        try {

            URL url = new URL(requestAddres);
            HttpURLConnection connection;
//            connection = (HttpURLConnection) url.openConnection(proxy);

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
            log.info(se.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {

        }
        //return null;
        return jsonString.toString();
    }

//    @Deprecated
//    public String getUpdates() {
//
//        String endOfAddr = Utils.INSTANCE.getTokenTg() + "/getUpdates?offset=0";
//        return this.sendRequest("GET", "https://api.telegram.org/bot", endOfAddr, "", "");
//
//    }


}