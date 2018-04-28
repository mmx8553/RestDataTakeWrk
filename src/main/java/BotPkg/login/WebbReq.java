package BotPkg.login;

import org.json.JSONObject;
import BotPkg.webbPkg.Webb;
import BotPkg.webbPkg.WebbException;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by OsipovMS on 06.04.2018.
 */
public class WebbReq {

    private String token;
    private Proxy proxy;


    private Webb webb ;

    public WebbReq(boolean hasProxy){
        this.token = null;

        Webb webbProxy = Webb.create();

        int respCode = 0;
        try {
            respCode = webbProxy.get("http://s-tmg.kamaz.org")
                    .ensureSuccess()
                    .asString()
                    .getStatusCode();
        } catch(Exception e){
            System.out.println(e.getMessage());
            this.proxy = null;

        }

        this.proxy = null;

        if (respCode == 200){
            this.proxy =  new Proxy(Proxy.Type.HTTP, new InetSocketAddress("s-tmg", 8080));
        }

//        if (!hasProxy) {
//            proxy = null;
//        }


        this.webb = Webb.create();
        webb.setProxy(this.proxy);
    }

    public boolean createToken(WebbReq webbReq ){
        RtData ce = new RtData();
        JSONObject result;
        String tokenUrl = ce.baseaddr+ce.requestTokenUrl;
        String JSONUsrPwd = ce.jsonLoginPassword;
        try {
            result = webbReq.getWebb().post(tokenUrl /*ce.baseaddr+ce.requestTokenUrl*/)
                    .body(new JSONObject(JSONUsrPwd /*ce.payload*/))
                    .retry(1, false) // at most one retry, don't do exponential backoff
                    .asJsonObject()
                    .getBody();

        }catch (Exception e){
            System.out.println("Error = WebbReq.createToken :"+ e.getMessage());
            e.printStackTrace();
            return false;
        }
        if (result.getBoolean("success")){
            webbReq.setToken(result.get("token").toString());
            System.err.println("token = " + webbReq.getToken());
            return true;
        }else{
            return false;
        }

    }



    public String getObjectGeoData( WebbReq webbReq , String id) throws WebbException{

        RtData ce = new RtData();
        JSONObject result = null;
        //String  result = null;
        String url = ce.baseaddr+ce.requestObjects+"/"+id;
        try {
                result = webbReq.getWebb().get(url)
                        .header("Authorization",new StringBuilder().append("Bearer ").append(webbReq.getToken()).toString())
                        .asJsonObject()
                        .getBody();


        }catch (WebbException we){
            System.out.println("Error = WebbReq.getUrlString:"+ we.getMessage());
            we.printStackTrace();

        }catch (Exception e) {
            System.out.println("Error = WebbReq.getUrlString:" + e.getMessage());
            e.printStackTrace();
        }

        //return (result == null ? "null" : result.toString());
        return result.toString();
    }



//    public Map<String,String> getBotRootObjectsMap(WebbReq webbReq){
//        RtData ce = new RtData();
//        System.out.println();
//        //ArrayList<String> ast = new ArrayList<String>();
//        Map<String,String> stringMap = new HashMap<>();
//
//
////            JSONObject result = null;
//            JSONArray result = null;
//            try {
//                result = webbReq.getWebb().get(ce.baseaddr+ce.requestObjects)
//                        .header("Authorization",new StringBuilder().append("Bearer ").append(webbReq.getToken()))
//                        .retry(1, false) // at most one retry, don't do exponential backoff
//                        .asJsonArray()
////                        .asJsonObject()
//                        .getBody();
//                //System.err.println("objects = " + result.toString());
//
//            }catch (Exception e){
//                System.out.println("Error - WebbReq = getObjectsList error =  :"+ e.getMessage());
//                e.printStackTrace();
//
//            }
//
//        try {
//            Iterator iterator = result.iterator();
//            String itemName;
//            String itemId;
//            JSONObject jsonObject;
//            while (iterator.hasNext()) {
//                jsonObject = new JSONObject(iterator.next().toString());
//
//                itemName = (String) jsonObject.get("name");
//                itemId = (String) jsonObject.get("_id");
//                //System.out.println(itemName);
//                stringMap.put(itemId,itemName);
//            }
//
//        }catch (Exception e){
//            System.out.println("Esc - 1get2Up3List = " + e.getMessage());
//        }
//
//        return stringMap;
//
//    }


//    public Map<String,String> getButtonMapToPrint(){
//        RtData ce = new RtData();
//        WebbReq webbReq = new WebbReq(false);
//
//        if (!webbReq.createToken(webbReq, ce.baseaddr + ce.requestTokenUrl, ce.jsonLoginPassword)) {
//            System.out.println("main = token problem");
//        }
//
//        Map <String,String> retMap =  webbReq.getBotRootObjectsMap(webbReq);
//
////        for (String nm:webbReq.getBotRootObjectsMap(webbReq)) {
////            System.out.println(nm);
////        }
//        return retMap;
//
//    }

    public static void main(String[] args) {

        RtData ce = new RtData();
        WebbReq webbReq = new WebbReq(false);

        if (!webbReq.createToken(webbReq )) {
            System.out.println("main = token problem");
        }

//        for (String nm:webbReq.getBotRootObjectsMap(webbReq).values().toArray(new String[1])) {
//            System.out.println(nm);
//        }
//

    }





    public Webb getWebb() {
        return webb;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
