package BotPkg.login;


import BotPkg.rootPkg.Utils;
import lombok.extern.java.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;


/**
 * Created by OsipovMS on 21.03.2018.
 */
@Log
public class RtData {

    @Deprecated
    public static final boolean useProxy = false; //true;

    public RtData() {
//        System.out.println("RtData constructor, token = " + tokenRequestNStore());
    }

    @Deprecated
    public String getToken() {
        return token;
    }

    @Deprecated
    public void setToken(String token) {
        this.token = token;
    }

    @Deprecated
    private String token;


    public String payload = "{\"BotPkg.login\":\"kamaz-maxim\",\"password\":\"nEWkj3_hG5\"}";
    public String jsonLoginPassword = "{\"BotPkg.login\":\"kamaz-maxim\",\"password\":\"nEWkj3_hG5\"}";
    public String baseaddr = "https://sandbox.rightech.io/api/v1";
    public String requestTokenUrl = "/auth/token";
    public String requestObjects = "/objects";


    @Deprecated
    public String tokenRequestNStore() {
        String tmpStr = "";
        String tmpToken = "";

        try {
            tmpStr = RtData.sendRequest("POST", baseaddr, requestTokenUrl, payload, null).toString();
            tmpToken = new JSONObject(tmpStr).get("token").toString();
        } catch (Exception e) {
            System.out.println("Exc = " + e.getMessage());

        }

        setToken(tmpToken);
        return tmpToken;

    }


    public static void maino(String[] args) {
        String ddr = "https://api.telegram.org/bot";

        System.out.println(sendRequest("GET",("https://api.telegram.org/bot" + Utils.INSTANCE.getTokenTg()+"/getme"),
                "",null,null));

    }


    public static String sendRequest(String method, String baseAddr, String appendAddr, String payload, String authToken) {

        String requestAddres = new StringBuilder().append(baseAddr).append(appendAddr).toString();

        StringBuffer jsonString = new StringBuffer();
        String proxx = "178.238.228.187:9090";

        String[] tok = proxx.split(":", 2);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(tok[0], new Integer(tok[1]).intValue()));


        try {

            URL url = new URL(requestAddres);
            HttpURLConnection connection;
//            proxy = null;
            connection = (HttpURLConnection) url.openConnection(proxy);

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
                log.info("Class = CONN-EXEC");
                System.err.println("" + connection.getResponseMessage());
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
        return jsonString.toString();
    }

    @Deprecated
    public String getUpdates() {

        String endOfAddr = Utils.INSTANCE.getTokenTg() + "/getUpdates?offset=0";
        return this.sendRequest("GET", "https://api.telegram.org/bot", endOfAddr, "", "");

    }

}