package BotPkg.webbPkg;

import BotPkg.rootPkg.Utils;
import BotPkg.login.ServerStatusException;

import java.io.IOException;

/**
 * Created by OsipovMS on 06.04.2018.
 */
@Deprecated
public class MxMain {
//    JSONObject result = webb
//            .get("https://example.com/api/request")
//            .retry(1, false) // at most one retry, don't do exponential backoff
//            .asJsonObject()
//            .getBody();


    public static void main(String[] args) throws IOException, ServerStatusException {

        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("s-tmg", 8080));

        Utils u = Utils.INSTANCE;

        Webb webb = Webb.create();
        //webb.setProxy(proxy);

        String st = "https://api.telegram.org/bot"+u.getTokenTg()+"/getme";


        //Webb webb = Webb.create();


        //JSONObject result = webb

//        HttpURLConnection conn = new ProxySetup().proxySetup(st);
//
//        conn.setDoInput(true);
//        conn.setDoOutput(true);
//        conn.setRequestMethod("GET");  /* GET , POST */
//        conn.setRequestProperty("Accept", "application/json");
//        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        //System.out.println(connection.getResponseCode());
//
//        if (conn.getResponseCode() != 200) {
//            throw new ServerStatusException(conn.getResponseMessage());
//        }
//
//
//        String line;
//        StringBuffer jsonString = new StringBuffer();
//        while ((line = br.readLine()) != null) {
//            jsonString.append(line);
//        }
//        br.close();
//
//
//
//        if (conn != null){
//            conn.disconnect();
//        }
//
//
//        System.out.println(jsonString.toString());





    }


}
