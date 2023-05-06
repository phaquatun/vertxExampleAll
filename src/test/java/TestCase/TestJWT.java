package TestCase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.VertxContextPRNG;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import java.util.Base64;
import java.util.UUID;

public class TestJWT extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
//        var jsonUser = new JsonObject().put("userName", "tungPham").put("pass", "changer987");

//        ok
//        JWTAuth providerHs = JWTAuth.create(vertx, new JWTAuthOptions()
//                .setJWTOptions(new JWTOptions().setExpiresInMinutes(60*24*4))
//                .addPubSecKey(new PubSecKeyOptions()
//                        .setAlgorithm("HS256")
//                        .setBuffer("cargroupsTjx03")));
//
//        
//        
//        String tokenHs = providerHs.generateToken(jsonUser);
//        String tokenBase64 = new String(Base64.getEncoder().encode(tokenHs.getBytes()));
//        System.out.println(">>>>>"+tokenHs);
//        System.out.println(VertxContextPRNG.current().nextInt(1000));
//
//        providerHs.authenticate(new JsonObject().put("token", tokenHs)).onComplete((e) -> {
//            if (e.failed()) {
//                e.cause().printStackTrace();
//                return;
//            }
//
//            var user = e.result().attributes();
//            boolean checkExpire =  e.result().expired();
//            System.out.println(">> check expire " +checkExpire);
//           
//            
//            System.out.println(user.toString());
//            if (e.result().get("userName").equals("tungPham")) {
//                System.out.println(e.result().principal().toBuffer());
//                return;
//            }
//
//            System.out.println("timer ok");
//        });
//        ok
        JWTAuthOptions config = new JWTAuthOptions().setKeyStore(new KeyStoreOptions()
                .setType("jceks")
                .setPath(System.getProperty("user.dir") + "\\keys\\keystore.jceks")
                .setPassword("ChangerTungpham92"))
                .setJWTOptions(new JWTOptions().setExpiresInMinutes(60 * 24 * 4));

        JWTAuth provider = JWTAuth.create(vertx, config);
//        JWTAuth.c
        var token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IjExaHVtYTU1NTU1NjB4NDE5IiwicGhvbmUiOiJudW1iZXI4IFBob25lIiwidGltZUNyIjoiMTY3MDkwMDU1NjgzNyIsInVpZCI6IjQ5MGRhMmNlLTc5MDAtNGIxMy1hODlmLTM0ZTcwMGZjMjU2MCIsImlhdCI6MTY3MDkwMDU1NiwiZXhwIjoxNjcxMjQ2MTU2fQ.C2fsbNyEreD9qCLVNRGqvR2Fd9LMZ8scyAniSs52OPk";
//        String token = provider.generateToken(jsonUser);

//        provider.
        System.out.println(token);

        provider.authenticate(new JsonObject().put("token", token), asynRes -> {
            System.out.println("accesstoken " +asynRes.result().attributes());
            if (asynRes.succeeded()) {
                System.out.println(">> " +asynRes.result().attributes());
                System.out.println(">> exp " + asynRes.result().expired());
//                System.out.println(">> "+ asynRes.result().);
                return;
            }
            
            asynRes.cause().printStackTrace();
            asynRes.cause().getMessage();
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new TestJWT());
//        String value = "btn.png";
//        System.out.println(value.split("\\.")[1]);
    }
}
