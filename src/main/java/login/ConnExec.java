package login;


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
public class ConnExec {

    public static final boolean useProxy = false ; //true;

    public ConnExec() {
//        System.out.println("ConnExec constructor, token = " + tokenRequestNStore());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public String payload = "{\"login\":\"kamaz-maxim\",\"password\":\"nEWkj3_hG5\"}";
    public String jsonLoginPassword = "{\"login\":\"kamaz-maxim\",\"password\":\"nEWkj3_hG5\"}";
    public String baseaddr = "https://sandbox.rightech.io/api/v1";
    public String requestTokenUrl = "/auth/token";
    public String requestObjects = "/objects";



    public String tokenRequestNStore(){

        String tmpStr =  "";
        String tmpToken = "";

        try{
            tmpStr =  ConnExec.sendRequest("POST",baseaddr, requestTokenUrl, payload, null).toString();
            tmpToken = new JSONObject(tmpStr).get("token").toString();
        }
        catch (Exception e){
            System.out.println("Exc = " + e.getMessage());
        }

        setToken(tmpToken);
        return tmpToken ;

    }




    public static String sendRequest(String method, String baseAddr, String requestUrl, String payload, String authToken) {

        String requestAddres = new StringBuilder().append(baseAddr).append(requestUrl).toString();

        StringBuffer jsonString = new StringBuffer();

        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("s-tmg", 8080));

//            URL url = new URL("https://ya.ru");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

            URL url = new URL(requestAddres);

            HttpURLConnection connection;

/**
 * Proxy - use or not
 */
            if (useProxy) {
                connection = (HttpURLConnection) url.openConnection(proxy);
            }else {
                connection = (HttpURLConnection) url.openConnection();
            }


            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(method);  /* GET , POST */
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (authToken != null){
                connection.setRequestProperty("Authorization",new StringBuilder()
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
                System.out.println(connection.getResponseMessage());
                throw new ServerStatusException(connection.getResponseMessage());
            }

            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();

            connection.disconnect();
        }catch (ServerStatusException se){
            //todo MSG to user that's  smthng  wrong - connection
            System.out.println(se.getMessage());

        } catch (Exception e) {
            //throw new RuntimeException(e.getMessage());
            System.out.println(e.getMessage());

        }finally {

        }

        return jsonString.toString();



    }

    public String getUpdates(){

        return "";
    }
}
