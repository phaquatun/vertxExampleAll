package Model.User.Handle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class HandleUserLogin extends AbstractVerticle {

    public void handleReqLogin(RoutingContext ctx) {

        JsonObject jsonBody = ctx.getBodyAsJson();
        System.out.println(jsonBody.encode());
//        String email = jso

    }

}
