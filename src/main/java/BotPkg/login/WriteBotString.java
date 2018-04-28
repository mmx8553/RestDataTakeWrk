package BotPkg.login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by OsipovMS on 05.04.2018.
 */
public class WriteBotString {



//    public static String msgSend(String chatId, String msg) {
//
//        StringBuilder sb = new StringBuilder();
//        String methodMsgChat = "/sendMessage?chat_id=";
//        String methodMsgTxt = "&text=";
//
//        sb.append("https://api.telegram.org/bot")
//                .append(token)
//                .append(methodMsgChat)
//                .append(chatId)
//                .append(methodMsgTxt)
//                .append(msg);
//
//        String requestUrl= sb.toString();
//
//        return requestUrl;
//    }



//    public static void maino(String[] args) {
//        String method = "";
//        method = "/getMe";
//        method = "/getUpdates";
//
//        String payload=null;
//        payload = null;
//        String requestUrl;
//
//        for(String st: BotPkg.login.MainClass.getUpList()){
//            requestUrl = msgSend(chatIdDef,st);
//            System.out.println(sendPostRequest(requestUrl, payload));
//        }
//    }

    @Deprecated
    public static String sendPostRequest(String requestUrl, String payload) {
        StringBuffer jsonString = new StringBuffer();
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("s-tmg", 8080));
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (payload != null) {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(payload);
                writer.close();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return jsonString.toString();
    }

}
