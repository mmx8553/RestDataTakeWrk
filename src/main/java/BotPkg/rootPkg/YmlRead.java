package BotPkg.rootPkg;

import lombok.extern.java.Log;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by OsipovMS on 17.04.2018.
 */
@Log
public class YmlRead {

    private String fileName = "c:\\\\1\\\\application.yml";

    YmlRead (String file){
        if (file != null) {
            this.fileName = file;
        }
    }

    public Map<String,String> getValues() throws FileNotFoundException{

        Yaml yaml = new Yaml();
        Map<String, Map<String, String>> values;
        Map<String,String> result = new HashMap<>();
        try {
            yaml.dump(yaml.load(new FileInputStream(new File(fileName))));
            values = (Map<String, Map<String, String>>) yaml
                    .load(new FileInputStream(new File(fileName)));

            for (String key : values.keySet()) {
                Map<String, String> subValues = values.get(key);
//                System.out.println(key);

                for (String subValueKey : subValues.keySet()) {
//                    System.out.println(String.format("\t%s = %s",subValueKey, subValues.get(subValueKey)));
                    result.put((String.format("%s.%s",key,subValueKey)), subValues.get(subValueKey));
                }
            }


        }catch(Exception e){

            e.printStackTrace();
        }
        log.info("YML = ok (загрузка данных конфигурации - успешно)");
        return result;

    }

}
