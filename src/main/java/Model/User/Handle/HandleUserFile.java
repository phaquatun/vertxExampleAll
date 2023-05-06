package Model.User.Handle;

import FormatPojo.FormatRes;
import FormatPojo.FormatUploadFile;
import Model.User.ConstanceUser;
import Model.User.ConstanceUser;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoGridFsClient;
import io.vertx.ext.mongo.UpdateOptions;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;

public class HandleUserFile extends AbstractVerticle {

    private MongoClient mongoClient;
    private MongoGridFsClient mongoGridFs;

    private JWTAuth provider;

    public HandleUserFile(JWTAuth provider) {
        this.provider = provider;
    }

    public HandleUserFile(MongoClient mongoClient, MongoGridFsClient mongoGridFs) {
        this.mongoClient = mongoClient;
        this.mongoGridFs = mongoGridFs;
    }

    /*
    ** handle up img check info cookie pvd :expired  ? cann't up img
     */
    public void hadnleResUpImg(Message<Object> msg, String collectionName) {
        var pojo = (FormatUploadFile) msg.body();

        var setFileUpLoad = pojo.getCtx().fileUploads();
        List<Future> listFut = new ArrayList<>();

        for (FileUpload fileUpload : setFileUpLoad) {
            //main  scrips 
            var fut = checkUserExis(pojo, fileUpload)
                    .compose(pojo::asynFile)
                    .compose(asynFile -> this.futUploadFs(asynFile, fileUpload))
                    .compose(idFs -> this.futUpDateInfoUser(collectionName, idFs, pojo.getUid(), fileUpload));

            listFut.add(fut);
        }
        
        CompositeFuture.all(listFut).onComplete((asynRes) -> {
            if (asynRes.succeeded()) {
                msg.reply(new FormatRes(null, "save img success", true).toJson());
                return;
            }

            msg.reply(new FormatRes(" valErr " + asynRes.cause().getMessage(), "save img err", false).toJson());
        });
    }

    Future<FileUpload> checkUserExis(FormatUploadFile pojo, FileUpload fileUpload) {
        return Future.future((promise) -> {
            var query = new JsonObject().put("uid", pojo.getUid()).put("email", pojo.getEmail());
            mongoClient.findOne(ConstanceUser.Collections_Driver, query, null).onComplete((asynRes) -> {
                if (asynRes.succeeded() & (asynRes.result() instanceof JsonObject)) {
                    promise.complete(fileUpload);
                    return;
                }
                promise.fail(asynRes.cause());
            });
        });
    }

    Future<String> futUploadFs(AsyncFile asynFile, FileUpload fileUpload) {

        return Future.future((promise) -> {
            String uploadFileName = fileUpload.uploadedFileName();
            String fileName = uploadFileName.contains("\\") ? uploadFileName.split("\\\\")[1] : uploadFileName;

            mongoGridFs.uploadByFileName(asynFile, fileName).onComplete((asynRes) -> {
                if (asynRes.succeeded()) {
                    promise.complete(asynRes.result());
                    return;
                }
                promise.fail(new FormatRes("err Upload Fs", "err Upload Fs " + asynRes.cause().getMessage(), false).toString());
            });
        });
    }

    Future<String> futUpDateInfoUser(String collectionName, String idFsImg, String uidDriver, FileUpload fileUpload) {
        return Future.future((promise) -> {

            var query = new JsonObject().put("uid", uidDriver);
            var update = new FormatUploadFile().jsonUpdate(fileUpload.name(), idFsImg, fileUpload.uploadedFileName());
            var findOp = new FindOptions();
            var updateOp = new UpdateOptions();
            mongoClient.findOneAndUpdateWithOptions(collectionName, query, update, findOp, updateOp).onComplete((asynRes) -> {
                if (asynRes.succeeded()) {
                    promise.complete();
                    return;
                }
                asynRes.cause().printStackTrace();
                promise.fail(new FormatRes("err update img driver", "some thing wrong", false).toString());
            });
        });
    }

    /*
    *** handle req evb uploadImg
     */
    public void handleReqUpImg(RoutingContext ctx) {
        futCheckAuthen(ctx)
                .compose(jsonAut -> this.futSendEvbUpImg(ctx, jsonAut))
                .result();
    }

    Future<JsonObject> futCheckAuthen(RoutingContext ctx) {
        return Future.future((promise) -> {
            String cookie = ctx.request().getCookie("pvd").getValue();
            provider.authenticate(new JsonObject().put("token", cookie)).onSuccess((user) -> {
                if (user.expired()) {
                    var formatRes = new FormatRes("your cookie somthing wrong expire", "check again cookie ", false).toString();
                    promise.fail(formatRes.toString());
                    ctx.response().end(formatRes.toString());

                    return;
                }
                promise.complete(user.attributes());
            })
                    .onFailure((ex) -> {
                        var formRes = new FormatRes("your cookie somthing wrong " + ex.getMessage(), "check again cookie ", false).toString();
                        promise.fail(formRes);
                        ctx.response().end(formRes);
                    });
        });
    }

    Future<Void> futSendEvbUpImg(RoutingContext ctx, JsonObject jsonAuth) {
        return Future.future((promise) -> {
            var setFile = ctx.fileUploads();
            if (setFile.size() > 5) {
                ctx.response().end(new FormatRes("big file count", "check again file ", false).toString());
                promise.complete();
                return;
            }

            ctx.vertx().eventBus().request(ConstanceUser.upImage, new FormatUploadFile(jsonAuth, ctx), (msg) -> {
                var jsonRes = (JsonObject) msg.result().body();
                ctx.request().response().end(jsonRes.toString());
            });
        });
    }

    /*
    ** handle doawlod image
     */
    public void handleReqDownLoadImg(RoutingContext ctx) {

        futCheckAuthen(ctx)
                .compose((jsonToken) -> this.sendImg(ctx))
                .result();
    }

    Future<Void> sendImg(RoutingContext ctx) {
        String path = ConstanceUser.pathFileUpload + ctx.pathParam("name");

        return ctx.request().response().sendFile(path).onComplete((asynRes) -> {
            if (asynRes.failed()) {
                asynRes.cause().printStackTrace();
            }
        });
    }

}
