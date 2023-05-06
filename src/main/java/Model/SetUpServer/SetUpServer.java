package Model.SetUpServer;

import Model.Admin.AdminVerticle;
import Model.Driver.DriverVerticle;
import Model.User.DAOUser;
import Model.User.UserVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CSPHandler;
import io.vertx.ext.web.handler.CSRFHandler;
import io.vertx.ext.web.handler.ChainAuthHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import java.net.CookieHandler;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class SetUpServer extends AbstractVerticle {

    private JWTAuth provider;
    private JWTAuth providerHS256;
    private Router router;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        configRouter()
                .compose(this::createHttpServer)
                .compose(this::deployVertices)
                .onComplete(this::resultStartServer);
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {

    }

    Future<Router> configRouter() {
        return Future.future((promise) -> {
            providerHS256 = JWTAuth.create(vertx, new JWTAuthOptions().setJWTOptions(new JWTOptions().setExpiresInMinutes(60 * 24 * 4))
                    .addPubSecKey(new PubSecKeyOptions().setAlgorithm("HS256").setBuffer("cargroupsTjx03"))
            );

            provider = JWTAuth.create(vertx, new JWTAuthOptions().setKeyStore(new KeyStoreOptions()
                    .setType("jceks")
                    .setPath(System.getProperty("user.dir") + "\\keys\\keystore.jceks")
                    .setPassword("ChangerTungpham92"))
                    .setJWTOptions(new JWTOptions().setExpiresInMinutes(60 * 24 * 4))
            );

            router = Router.router(vertx);

            SessionStore sessionStoreV1 = LocalSessionStore.create(vertx, "chna");
            router.route().handler(SessionHandler.create(sessionStoreV1)
                    .setCookieHttpOnlyFlag(true)
                    .setSessionCookieName("vcar")
                    .setCookieMaxAge(60 * 24 * 4).
                    setMinLength(30)
            );

            router.route().handler(BodyHandler.create());

            // cors for test 
//            router.route().handler(CSPHandler.create().addDirective("default-src", "*.trusted.com"));
//            router.route().handler(CSRFHandler.create(vertx, Base64.getEncoder().encodeToString("cargroupSatand".getBytes())).setCookieName("cf"));
            router.route().handler(CorsHandler.create("*")
                    .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                    .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                    .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                    .allowedHeader("Access-Control-Allow-Credentials")
                    .allowedHeader("Access-Control-Allow-Headers")
                    .allowedHeader("Content-Type"));
            promise.complete(router);
        });
    }

    Future<Router> createHttpServer(Router router) {
        return Future.future((promise) -> {
            ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file")
                    .setFormat("json")
                    .setConfig(new JsonObject().put("path", System.getProperty("user.dir") + "\\val\\configStore.json"));

            ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(fileStore));

            retriever.getConfig().onSuccess((asynReult) -> {
                getVertx().createHttpServer().requestHandler(router).listen(asynReult.getJsonObject("http").getInteger("port"));
            })
                    .onFailure((e) -> {
                        e.getCause().printStackTrace();
                    });
            promise.complete(router);
        });
    }

    /*
    ** deploy all vertices project car
     */
    Future<Void> deployVertices(Router router) {
        int totalCore = Runtime.getRuntime().availableProcessors();
        DeploymentOptions options = new DeploymentOptions().setHa(true).setWorkerPoolSize(50)
                .setWorker(true)
                .setInstances(totalCore * 2);

        var admin = Future.future((promise) -> promise.complete(vertx.deployVerticle(() -> new AdminVerticle(router), options)));

        var user = Future.future(promise -> promise.complete(vertx.deployVerticle(() -> new DriverVerticle(router, provider, providerHS256), options)));
        var dataUser = Future.future((promise) -> promise.complete(vertx.deployVerticle(() -> new DAOUser(), options)));

        var driver = Future.future(promise -> promise.complete(vertx.deployVerticle(() -> new UserVerticle(router, provider, providerHS256), options)));

        List<Future> listFutre = Arrays.asList(admin, user, driver, dataUser);
        return CompositeFuture.all(listFutre).mapEmpty();
    }

    void resultStartServer(AsyncResult<Void> asynResult) {

        if (asynResult.succeeded()) {
            System.out.println("create server Success");
        }
        if (asynResult.failed()) {
            System.out.println("create server fail " + asynResult.cause().getMessage());
        }
    }

}
