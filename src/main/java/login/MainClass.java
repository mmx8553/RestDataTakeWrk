package login;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * connect to RighTech - and get list of 1st level  objects
 */


/**
 * Created by OsipovMS on 21.03.2018.
 */
public class MainClass {



    public  static String payload = "{\"login\":\"kamaz-maxim\",\"password\":\"nEWkj3_hG5\"}";
    public  static String baseaddr = "https://sandbox.rightech.io/api/v1";
    public  static String requestUrl = "/auth/token";
//        String requestUrl="http://search.twitter.com/search.json?q=apple";


    public static void main(String[] args) {

    }


    public static ArrayList<String> getUpList(){
        ConnExec ce = new ConnExec();
        System.out.println();
        ArrayList<String> ast = new ArrayList<String>();
        try {

            String tmpStr =  ConnExec.sendRequest("POST",baseaddr, requestUrl, payload, null).toString();
            //System.out.println(tmpStr);

            String tmpToken = new JSONObject(tmpStr).get("token").toString();
            //System.out.println(new JSONObject(tmpStr).get("token"));

            tmpStr =  ConnExec.sendRequest("GET",baseaddr, "/objects", null, tmpToken).toString();
            //System.out.println("str all objects = " + tmpStr);

            //JSONParser JsPrsr = new JSONParser();
            JSONArray JsnArr= new JSONArray(tmpStr);

            Iterator iterator = JsnArr.iterator();
            String itemName;
            while (iterator.hasNext()) {
                //System.out.println(iterator.next().toString());
                //System.out.println(new JSONObject(iterator.next().toString()).get("name"));
                itemName = (String) new JSONObject(iterator.next().toString()).get("name");
                ast.add(itemName);
            }

//         } catch (ParseException e) {
//            System.out.println("parse Exc = " + e.getMessage());
//            //e.printStackTrace();
        }
        catch (Exception e){
            System.out.println("Exc = " + e.getMessage());
        }

        return ast;

    }
}
