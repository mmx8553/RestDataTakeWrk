package BotPkg.rootPkg;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import BotPkg.login.ServerStatusException;
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
        try {
            maino();
        }catch(Exception e){

        }
    }
    public static void maino() throws IOException, ServerStatusException, InterruptedException {
        //создание сингтона
        Utils u = Utils.INSTANCE;


        int localDelay = 1500;

        //System.out.println(Utils.INSTANCE.getJsonRtLoginPassword());

        ProxConn pc = new ProxConn();

        List<Update> lu = new ArrayList<>();
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

                        int lastMessageNom = 1;
                        System.out.println("messages = " + upd.size());
                        System.out.println("first one text IS = " + firsstUpd.getMessage().getText());
                    } else {
                        if (hasMsg) {
                            System.out.println("no messages");
                            hasMsg = false;
                            localDelay = 2300;
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
