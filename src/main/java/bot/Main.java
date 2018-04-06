package bot;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;


/**
 * Created by mmx on 06.02.2018.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();


        // Instantiate Telegram Bots API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try{

            HttpHost proxy = new HttpHost("s-tmg",8080,"http");//new Proxy(Proxy.Type.HTTP, new InetSocketAddress("s-tmg", 8080));
            DefaultBotOptions instance = ApiContext
                    .getInstance(DefaultBotOptions.class);
            RequestConfig rc = RequestConfig.custom()
                    .setProxy(proxy).build();

            instance.setRequestConfig(rc);
            //bot = new Bot(instance);



//            telegramBotsApi.registerBot(new KmzBot(instance));//new KmzData(new ConnExec())));
            telegramBotsApi.registerBot(new KmzBot());
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }



    }
}
