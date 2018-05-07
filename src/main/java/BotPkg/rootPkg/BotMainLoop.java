package BotPkg.rootPkg;

import BotPkg.login.ServerStatusException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.java.Log;
import org.telegram.telegrambots.api.objects.Update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OsipovMS on 25.04.2018.
 */
@Log
public class BotMainLoop {
    public static void main(String[] args) {

        Utils uu = Utils.INSTANCE;

        try {
            maino();


        }catch(Exception e){

        }
    }



    public static void maino() throws IOException, ServerStatusException, InterruptedException {
        //создание сингтона
        Utils u = Utils.INSTANCE;
        u.getRtData().init(u);
        ProxConn pc = u.getProxyConnection();

        BotMessaging bm = u.getBotMessaging();

        u.setUseProx(true);


        int localDelay = 1500;

        //System.out.println(Utils.INSTANCE.getJsonRtLoginPassword());

        List<Update> lu = new ArrayList<Update>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;

        boolean hasMsg = false;
        try {

            while (true) {

                rootNode = mapper.readValue(pc.getBotUpdates(), JsonNode.class); // парсинг текста


                if (rootNode.get("ok").asBoolean()) {
                    JsonNode resJson = rootNode.get("result");
                    ObjectReader reader = mapper.readerFor(new TypeReference<ArrayList<Update>>() {});
                    ArrayList<Update> upd = reader.readValue(resJson);

                    if (!upd.isEmpty()) {
                        localDelay = 100;
                        hasMsg = true;
                        Update firsstUpd = upd.get(0);
                        u.setMaxMessageOffset(firsstUpd.getUpdateId() + 1);

//                        int lastMessageNom = 1;
                        System.out.println("messages = " + upd.size());
                        //System.out.println("first one text IS = " + firsstUpd.getMessage().getText());
//GOT CALLBACKQUERY
                        if (firsstUpd.hasCallbackQuery()){
                            String cbData = firsstUpd.getCallbackQuery().getData();
                            System.out.println("111 =  = CallbackDATA = " + cbData);
                            Long chatId = firsstUpd.getCallbackQuery().getMessage().getChatId();
                            bm.processAfterGotCallback(chatId, cbData);
                        }
// GOT TEXT MESSAGE
                        if (firsstUpd.hasMessage()  && firsstUpd.getMessage().hasText()) {
                            String txtMsg = firsstUpd.getMessage().getText();
                            Long chatId = firsstUpd.getMessage().getChatId();
                            bm.processAfterGotTextMessage(chatId, txtMsg);

                            System.out.println("000 = (получено сообщение) = " + txtMsg + "     \n\tCHAT = " + chatId.toString());

                        }
                    } else {
                        if (hasMsg) {
                            System.out.println(" - - - no messages  - - -  waiting for new one");
                            hasMsg = false;
                            localDelay = 1700;
                        }
                    }

                    Thread.sleep(localDelay);

                } else {
                    log.info("Какой-то не такой ответ от сервера");
                }

            }
        }catch (Exception e){
            log.info(e.getMessage());
        }


    }


}
