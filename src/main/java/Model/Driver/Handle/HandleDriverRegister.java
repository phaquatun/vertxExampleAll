package Model.Driver.Handle;

import FormatPojo.FormatRes;
import Model.Driver.ConstanceDriver;
import Model.SetUpServer.ConstantManager;
import Model.User.ConstanceUser;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.Cookie;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoGridFsClient;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HandleDriverRegister extends AbstractVerticle {

    private MongoClient mongoClient;

    public HandleDriverRegister() {
    }

    public HandleDriverRegister(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /*
    *** handle Req regis    use cookie pvd check upload img
     */
    public void handleReqRegister(RoutingContext ctx, JWTAuth provider, JWTAuth providerHS256) {
        String uuid = UUID.randomUUID().toString();
        JsonObject jsonBody = ctx.getBodyAsJson().put("timeCr", System.currentTimeMillis()).put("uid", uuid);

        var jsonTokenProviderHS = new JsonObject().put("email", jsonBody.getString("email")).put("phone", jsonBody.getString("phone"))
                .put("timeCr", jsonBody.getString("timeCr"));
        var jsonTokenProvider = jsonTokenProviderHS.put("uid", uuid);

        ctx.vertx().eventBus().request(ConstanceDriver.registerDriver, jsonBody, (msg) -> {
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
    ** handle Res Register User
     */
    public void handleResRegisterUser(Message<Object> msg) {

        var jsonReq = (JsonObject) msg.body();

        mongoClient.save(ConstanceUser.Collections_Driver, jsonReq, (asynRes) -> {
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

}
