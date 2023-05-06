package Model.Driver;

import FormatPojo.FormatUploadFile;
import Model.Driver.Handle.HandleDriverRegister;
import Model.User.Handle.UploadImgCodeC;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;

public class DriverVerticle extends AbstractVerticle {

    private Router router;
    private JWTAuth provider;
    private JWTAuth providerHS256;

    public DriverVerticle(Router router, JWTAuth provider, JWTAuth providerHS256) {
        this.router = router;
        this.provider = provider;
        this.providerHS256 = providerHS256;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        vertx.eventBus().registerDefaultCodec(FormatUploadFile.class, new UploadImgCodeC());
        router.post("/register/*").handler(BodyHandler.create());

        router.post("/register/driver").handler(BodyHandler.create());
        router.post("/register/driver").handler(this::handleRegisterUser);

    }

    void handleRegisterUser(RoutingContext ctx) {

        new HandleDriverRegister().handleReqRegister(ctx, provider, providerHS256);
    }

   
}
