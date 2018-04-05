package utils;

import login.ServerStatusException;

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
public class ProxConn {
    public static void main(String[] args) throws IOException,Exception {

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("s-tmg", 8080));

        URL url = new URL("http://myip.dnsdynamic.org/");
        //with proxy
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

        //no proxy
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

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



        if (connection != null){
            connection.disconnect();
        }


        System.out.println(jsonString.toString());

    }
}
