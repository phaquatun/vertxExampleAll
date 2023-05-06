package Model.User;

import FormatPojo.FormatUserRegister;
import Model.User.Handle.HandleUserFile;
import Model.Driver.Handle.HandleDriverRegister;
import Model.User.Handle.HandleUserRegister;
import Model.User.Handle.UserRegisterCodeC;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class UserVerticle extends AbstractVerticle {

    private Router router;
    private JWTAuth provider;
    private JWTAuth providerHS256;

    public UserVerticle(Router router, JWTAuth provider, JWTAuth providerHS256) {
        this.router = router;
        this.provider = provider;
        this.providerHS256 = providerHS256;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        vertx.eventBus().registerDefaultCodec(FormatUserRegister.class, new UserRegisterCodeC());
        router.post("/user/upload/*").handler(
                BodyHandler.create()
                        .setBodyLimit(200000)
                        .setHandleFileUploads(true)
        );

        router.post("/user/upload/img").handler(this::handleUpImage);
        router.get("/download/:name").handler(this::handerDoawloadImage);
        router.post("/register/user/").handler(BodyHandler.create()).handler(this::handleRegisterUser);
        router.post("/login/user").handler(BodyHandler.create()).handler(this::handleLogin);
    }

    /*
    *** login user
     */
    void handleLogin(RoutingContext ctx) {

    }

    /*
    ***  register user
     */
    void handleRegisterUser(RoutingContext ctx) {

        new HandleUserRegister().handleReqRegis(ctx, provider, providerHS256);
    }

    /*
    *** use cookie pvd check upload img
     */
    private void handleUpImage(RoutingContext ctx) {

        new HandleUserFile(provider).handleReqUpImg(ctx);
    }

    void handerDoawloadImage(RoutingContext ctx) {

        new HandleUserFile(provider).handleReqDownLoadImg(ctx);
    }

}
