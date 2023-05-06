package Model.User.Handle;

import FormatPojo.FormatRes;
import FormatPojo.FormatUserRegister;
import Model.Driver.ConstanceDriver;
import Model.User.ConstanceUser;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.Cookie;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoGridFsClient;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HandleUserRegister extends AbstractVerticle {

    private MongoClient mongoClient;

    private MongoGridFsClient mongoGridFs;

    public HandleUserRegister() {
    }

    public HandleUserRegister(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /*
    ** send req
     */
    public void handleReqRegis(RoutingContext ctx, JWTAuth provider, JWTAuth providerHS256) {
        String uuid = UUID.randomUUID().toString();
        JsonObject jsonBody = ctx.getBodyAsJson().put("timeCr", System.currentTimeMillis()).put("uid", uuid);

        var jsonTokenProviderHS = new JsonObject().put("email", jsonBody.getString("email")).put("phone", jsonBody.getString("phone"))
                .put("timeCr", jsonBody.getString("timeCr"));
        var jsonTokenProvider = jsonTokenProviderHS.put("uid", uuid);

        ctx.vertx().eventBus().request(ConstanceUser.registerUser, new FormatUserRegister(jsonBody, ctx), (msg) -> {
            var jsonRes = (JsonObject) msg.result().body();
            if (jsonRes.getBoolean("success")) {
                ctx.response().addCookie(Cookie.cookie("pvd", provider.generateToken(jsonTokenProvider)))
                        .addCookie(Cookie.cookie("hs", providerHS256.generateToken(jsonTokenProviderHS.put("id", jsonRes.getString("id")))))
                        .end(jsonRes.toString());
                return;
            }

            ctx.response().end(jsonRes.toString());
        });
    }

    /*
    ** handle response
     */
    public void handleResRegist(Message<Object> msg) {

        FormatUserRegister pojo = (FormatUserRegister) msg.body();
        var jsonReq = pojo.getJsonReq();
        System.out.println(">> check handleResRegist " + jsonReq);

        var queryMail = new JsonObject().put("email", jsonReq.getString("email"));
        var queryPhone = new JsonObject().put("phone", jsonReq.getString("phone"));

        CompositeFuture.all(checkEmailReg(queryMail), checkPhone(queryPhone))
                .onComplete((composite) -> this.saveInfoUser(jsonReq, composite, msg))
                .result();
    }

    Future<String> checkEmailReg(JsonObject queryEmail) {
        return Future.future((promise) -> {
            mongoClient.findOne(ConstanceUser.Collections_User, queryEmail, null, (asynRes) -> {
                if (asynRes.failed()) {
                    promise.fail("err find email");
                    return;
                }
                if (asynRes.result() instanceof JsonObject) {
                    promise.fail("email khoản đã tồn tại");
                    return;
                }

                promise.complete("success unduplicate email");
            });
        });
    }

    Future<String> checkPhone(JsonObject queryPhone) {
        return Future.future((promise) -> {
            mongoClient.findOne(ConstanceUser.Collections_User, queryPhone, null, (asynRes) -> {
                if (asynRes.failed()) {
                    promise.fail("err find phone");
                    return;
                }

                if (asynRes.result() instanceof JsonObject) {
                    promise.fail("phone khoản đã tồn tại");
                    return;
                }
                promise.complete("success unduplicate phone");
            });
        });
    }

    void saveInfoUser(JsonObject jsonReq, AsyncResult<CompositeFuture> asynComposite, Message<Object> msg) {

        if (asynComposite.succeeded()) {
            mongoClient.save(ConstanceUser.Collections_User, jsonReq, (asynRes) -> {
                if (asynRes.succeeded()) {
                    msg.reply(new FormatRes(null, new JsonObject().put("des", "tạo tài khoản thành công").toString(), true)
                            .toJson()
                            .put("id", asynRes.result())
                    );
                    return;
                }

                msg.reply(new FormatRes("lưu thông tin thất bại", "vui lòng liên hệ support", false).toJson());
            });
        }

        if (asynComposite.failed()) {
            msg.reply(new FormatRes(asynComposite.cause().getMessage(), "vui lòng chọn email\\phone khác", false).toJson());
        }

    }

}
