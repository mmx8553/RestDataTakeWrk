package EmptyBot;


//import org.apache.commons.httpclient.HttpHost;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by OsipovMS on 02.04.2018.
 */

@Profile("profile1")
@SpringBootApplication
//@PropertySource("classpath:root/test.props")

public class BotMain {
    //Abilitybot




    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

//        System.out.println();

        Utils utils = Utils.getInstance();
        TelegramLongPollingBot myBot = new IPBot();



        try{
            telegramBotsApi.registerBot(myBot);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }



    }
}
