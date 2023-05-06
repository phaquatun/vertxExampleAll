package TestCase;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class TestCase {

    public static void main(String[] args) throws InterruptedException {
       var listPath  = new ArrayList<String>();
       listPath.add("sldkls");
       listPath.add("iuyrt98");
       listPath.add("jer984");
       
       var json = new JsonObject().put("arrPath", new JsonArray(listPath));
        System.out.println(json.toString());
       
    }

    public void run() {
//    new 
    }

    private String encode(String key) {

        return Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }
}
