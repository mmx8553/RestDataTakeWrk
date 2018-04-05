package EmptyBot;


//import org.apache.commons.httpclient.HttpHost;


import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by OsipovMS on 02.04.2018.
 */
public class Main {
    //Abilitybot
    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();


        // Instantiate Telegram Bots API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();


        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("s-tmg", 8080));
        HttpHost proxy = new HttpHost("s-tmg", 8080, "http");

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        //RequestConfig.custom();


        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy( new HttpHost("10.0.0.198" , 8080, "http"))
//                .setSocketTimeout(SOCKET_TIMEOUT)
//                .setConnectTimeout(SOCKET_TIMEOUT)
//                .setConnectionRequestTimeout(SOCKET_TIMEOUT)
                .build();


        defaultBotOptions.setRequestConfig(requestConfig);
        defaultBotOptions.getRequestConfig().getProxy().toString();



        //rcc =  new RequestConfig.Builder().setProxy();//   requestConfig = new RequestConfig();
                //.custom().build();
        //requestConfig = new RequestConfig(1,new Proxy("HTTP","s-tmg:8080"));
        //defaultBotOptions.setRequestConfig();

//        TelegramLongPollingBot myBot = new IPBot(defaultBotOptions);
        TelegramLongPollingBot myBot = new IPBot();

//        myBot.getOptions().setProxyHost("http://myproxy.com");
//        myBot.getOptions().setProxyPort(8080);

//        System.out.println(myBot.getOptions().getRequestConfig().getProxy());//  setProxyHost("http://myproxy.com");

        try{
            telegramBotsApi.registerBot(myBot);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }



    }
}
