package webbPkg;

import org.json.JSONObject;

/**
 * Created by OsipovMS on 06.04.2018.
 */
public class MxMain {
    Webb webb = Webb.create();
    JSONObject result = webb
            .get("https://example.com/api/request")
            .retry(1, false) // at most one retry, don't do exponential backoff
            .asJsonObject()
            .getBody();
}
