package FormatPojo;

import Model.User.ConstanceUser;
import io.vertx.core.Future;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoGridFsClient;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

public class FormatUploadFile {

    JsonObject jsonReq;
    RoutingContext ctx;
    String email, uid;

    public FormatUploadFile() {
    }

    public FormatUploadFile(JsonObject jsonReq, RoutingContext ctx) {
        this.jsonReq = jsonReq;
        this.ctx = ctx;

        var token = jsonReq.getJsonObject("accessToken");
        this.email = token.getString("email");
        this.uid = token.getString("uid");
    }

    public JsonObject getJsonReq() {
        return jsonReq;
    }

    public RoutingContext getCtx() {
        return ctx;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public JsonObject jsonUpdate(String name, String idFs, String uploadedFileName) {
        String fileName = uploadedFileName.contains("\\") ? uploadedFileName.split("\\\\")[1] : uploadedFileName;

        var update = new JsonObject();
        update.put("$set", new JsonObject().put("image." + name,
                new JsonObject().put( "idGridFs", idFs).put("fileName", fileName)
        ));

        return update;
    }

    public Future<AsyncFile> asynFile(FileUpload fileUpload) {
        return Future.future((promise) -> {
            String uploadedFileName = ConstanceUser.pathProject + fileUpload.uploadedFileName();

            ctx.vertx().fileSystem().open(uploadedFileName, new OpenOptions()).onComplete((asynRes) -> {
                if (asynRes.succeeded()) {
                    promise.complete(asynRes.result());
                    return;
                }
                promise.fail(new FormatRes("err opneFile ", "err openFile", false).toString());
            });
        });
    }

    @Override
    public String toString() {
        return "FormatUploadTest{" + "pathFile=" + jsonReq.toString() + ", ctx=" + ctx + '}';
    }

}
