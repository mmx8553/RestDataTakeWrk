package RootPkg;

import org.json.JSONObject;
import webbPkg.Webb;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by OsipovMS on 05.04.2018.
 */
@SuppressWarnings("ALL")
public class MainLoop {

    public static void main(String[] args) throws Exception {

        Logger.getLogger(MainLoop.class.getName()).log(java.util.logging.Level.SEVERE, "Бот. Начало работы");


        /**
         * LAMBDA  EXAMPLE
         */
//        Map<String, Integer> items = new HashMap<>();
//        items.put("A", 10);        items.put("B", 20);        items.put("C", 30);        items.put("D", 40);        items.put("E", 50);        items.put("F", 60);
//
//
//        for (Map.Entry<String, Integer> entry : items.entrySet()) {
//            System.out.println("Item111 : " + entry.getKey() + " Count111 : " + entry.getValue());
//        }
//
//        items.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));
//
//        items.forEach((k,v)->{
//            System.out.println("Item : " + k + " Count : " + v);
//            if("E".equals(k)){
//                System.out.println("Hello E");
//            }
//        });
//
//

        Webb webb = Webb.create();

    try {


        JSONObject result = webb
            .get("https://api.telegram.org/bot523022396:AAFryNlfFVL5WOTAD35BM4bBL832zis_ERE/getUpdates")
//                .param("origin", new GeoPoint(47.8227, 12.096933))
//                .param("destination", new GeoPoint(47.8633, 12.215533))
            .param("data","{'offset': offset  748, 'limit': 0, 'timeout': 0}")
            .ensureSuccess()
            .asJsonObject()
            .getBody();

    //JSONArray routes = result.getJSONArray("routes");
        System.err.println(result);
    } catch (Exception e){
        System.out.println(e.getMessage());
        Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, "bot! = alarm");
    }



        JSONObject params = new JSONObject();
        params.put("key1", "key-01");
        params.put("key2", "long-key-02");

        String ts1 = "";
        //obj.put("params", params);


        JSONObject result = webb
                .get("https://api.telegram.org/bot523022396:AAFryNlfFVL5WOTAD35BM4bBL832zis_ERE/getUpdates")
//                .param("origin", new GeoPoint(47.8227, 12.096933))
//                .param("destination", new GeoPoint(47.8633, 12.215533))
                .param("data","{'offset': offset  748, 'limit': 0, 'timeout': 0}")
                .ensureSuccess()
                .asJsonObject()
                .getBody();

        //JSONArray routes = result.getJSONArray("routes");
        System.err.println(result);



        String st = "";







        Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, "bot! = All OK = FINISHING.");

    }
}
