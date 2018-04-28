package BotPkg.rootPkg;

import BotPkg.login.ServerStatusException;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 *  DDNS - SHOW
 */

/**
 * Created by OsipovMS on 02.04.2018.
 */
@Log
public class ProxConn {
    private Utils u = Utils.INSTANCE;
    private String proxx = "178.238.228.187:9090";


    public String getMe() throws IOException, ServerStatusException {

        String reqUrlString = "https://api.telegram.org/bot" + u.getTokenTg() + "/getme";

        String result = null;

        result = getBotResponse(reqUrlString);
        if (result != null){
            return result;
        }

        return null;
    }

    public String getBotUpdates() throws IOException, ServerStatusException {

        int offsetMax = u.getMaxMessageOffset();

        String reqUrlString = String.format("https://api.telegram.org/bot%s%s%s",u.getTokenTg() ,"/getUpdates?offset=",offsetMax);

        String result = null;

        result = getBotResponse(reqUrlString);
        if (result != null){
            return result;
        }

        return null;

    }



    public String getBotResponse(String reqUrl) throws IOException, ServerStatusException {

        String[] tok = proxx.split(":", 2);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(tok[0],new Integer( tok[1]).intValue()));

        String result = null;

        try {
            URL url = new URL(reqUrl);

            //with proxy
            HttpURLConnection connection;
            if (u.isUseProx()) {
                connection = (HttpURLConnection) url.openConnection(proxy);
            }else{
                connection = (HttpURLConnection) url.openConnection();
            }

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");  /* GET , POST */
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //System.out.println(connection.getResponseCode());

            if (connection.getResponseCode() != 200) {
                throw new ServerStatusException(connection.getResponseMessage());
            }


            String line;
            StringBuffer jsonString = new StringBuffer();
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();


            if (connection != null) {
                connection.disconnect();
            }

            result = jsonString.toString();

        }catch (Exception e){
            log.info(e.getMessage());
        }

        return result;
    }
}
