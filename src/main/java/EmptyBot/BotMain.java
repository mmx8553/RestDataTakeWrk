package EmptyBot;


//import org.apache.commons.httpclient.HttpHost;


import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by OsipovMS on 02.04.2018.
 */
public class BotMain {
    //Abilitybot
    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();


        TelegramLongPollingBot myBot = new IPBot();


        try{
            telegramBotsApi.registerBot(myBot);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }



    }
}
