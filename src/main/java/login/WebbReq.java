package login;

import org.json.JSONArray;
import org.json.JSONObject;
import webbPkg.Webb;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by OsipovMS on 06.04.2018.
 */
public class WebbReq {
    private String token;
    private Proxy proxy;


    private Webb webb ;

    public WebbReq(){
        this.token = null;
        this.proxy = null; //new Proxy(Proxy.Type.HTTP, new InetSocketAddress("s-tmg", 8080));
        this.webb = Webb.create();
        webb.setProxy(this.proxy);
    }

    public boolean createToken(WebbReq webbReq, String tokenUrl, String JSONUsrPwd){
        ConnExec ce = new ConnExec();
        JSONObject result;
        try {
            result = webbReq.getWebb().post(tokenUrl /*ce.baseaddr+ce.requestTokenUrl*/)
                    .body(new JSONObject(JSONUsrPwd /*ce.payload*/))
                    .retry(1, false) // at most one retry, don't do exponential backoff
                    .asJsonObject()
                    .getBody();

        }catch (Exception e){
            System.out.println("WebbReq.createToken :"+ e.getMessage());
            e.printStackTrace();
            return false;
        }
            if (result.getBoolean("success")){
                webbReq.setToken(result.get("token").toString());
                System.err.println(webbReq.getToken());
                return true;
            }else{
                return false;
            }

    }


    public  ArrayList<String> getUpList(WebbReq webbReq){
        ConnExec ce = new ConnExec();
        System.out.println();
        ArrayList<String> ast = new ArrayList<String>();


//            JSONObject result = null;
            JSONArray result = null;
            try {
                result = webbReq.getWebb().get(ce.baseaddr+ce.requestObjects)
                        .header("Authorization",new StringBuilder().append("Bearer ").append(webbReq.getToken()))
                        .retry(1, false) // at most one retry, don't do exponential backoff
                        .asJsonArray()
//                        .asJsonObject()
                        .getBody();
                System.out.println("objects = " + result.toString());

            }catch (Exception e){
                System.out.println("getObjectsList error =  :"+ e.getMessage());
                e.printStackTrace();

            }

        try {
            Iterator iterator = result.iterator();
            String itemName;
            while (iterator.hasNext()) {
                itemName = (String) new JSONObject(iterator.next().toString()).get("name");
                //System.out.println(itemName);
                ast.add(itemName);
            }

        }catch (Exception e){
            System.out.println("Esc - getUpList = " + e.getMessage());
        }

        return ast;

    }


    public String[] getButtonListToPrint(){
        ConnExec ce = new ConnExec();
        WebbReq webbReq = new WebbReq();

        if (!webbReq.createToken(webbReq, ce.baseaddr + ce.requestTokenUrl, ce.jsonLoginPassword)) {
            System.out.println("main = token problem");
        }

        String[] retStrArray =  webbReq.getUpList(webbReq).toArray(new String[0]);

//        for (String nm:webbReq.getUpList(webbReq)) {
//            System.out.println(nm);
//        }
        return retStrArray;

    }

    public static void main(String[] args) {

        ConnExec ce = new ConnExec();
        WebbReq webbReq = new WebbReq();

        if (!webbReq.createToken(webbReq, ce.baseaddr + ce.requestTokenUrl, ce.jsonLoginPassword)) {
            System.out.println("main = token problem");
        }

        for (String nm:webbReq.getUpList(webbReq)) {
            System.out.println(nm);
        }


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
