package Model.Admin;

import Model.SetUpServer.ConstantManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class AdminVerticle extends AbstractVerticle {

    private Router router;

    public AdminVerticle(Router router) {
        this.router = router;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        router.get("/login/admin/*").handler(StaticHandler.create().setWebRoot("frontEnd\\admin\\")
                .setIndexPage("index.html")
                .setDefaultContentEncoding("UTF-8")
        );
        router.get("/admin/manager/*").handler(StaticHandler.create()
                .setWebRoot("frontEnd\\admin\\")
                .setIndexPage("index.html")
                .setDefaultContentEncoding("UTF-8")
        );
        router.post("/cdx/admin/*").handler(BodyHandler.create());
        router.post("/cdx/admin/reg/chanager").handler(this::handleRegistAdmin);
        router.post("/cdx/admin/login").handler((e) -> {
        });

    }

    /*
    *** eventbus is custom 
    *** end request for client 
     */
    // send request for customer
    private void handleLoginAdmin(RoutingContext ctx) {
        JsonObject json = ctx.getBodyAsJson();

    }

    private void handleRegistAdmin(RoutingContext ctx) {
        JsonObject jsonBody = ctx.getBodyAsJson();

        vertx.eventBus().request(ConstantManager.registerAdmin, jsonBody, (msg) -> {
            ctx.request().response().end((String) msg.result().body());
        });
    }

    void handleHelloAdmin(RoutingContext ctx) {
        vertx.eventBus().request("admin.hello", "", (msg) -> {
            ctx.request().response().end("hello vertx tungpham  ...");
        });
    }

}
