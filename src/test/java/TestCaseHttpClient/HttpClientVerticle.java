

package TestCaseHttpClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.ext.web.client.WebClient;



public class HttpClientVerticle extends AbstractVerticle{

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
       
        WebClient client = WebClient.create(vertx);
//        client.post(8080, "", requestURI)
        
    }
    
    
    
}
