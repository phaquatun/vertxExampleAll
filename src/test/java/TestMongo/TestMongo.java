package TestMongo;

import Model.SetUpServer.ConstantManager;
import Model.User.ConstanceUser;
import com.mongodb.client.model.CollationAlternate;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.mongo.CollationOptions;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoGridFsClient;

public class TestMongo extends AbstractVerticle {

    private MongoClient mongoClient;
    MongoGridFsClient mongoGridFsClient;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new TestMongo());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        createMongoDb()
                //                .compose(this::createIndex)
                //                .compose(this::find)
                //                .compose(this::saveInfo)
                .compose(this::findOne)
//                .compose(this::createGridFs)
//                .compose(this::uploadImage)
//                .compose(this::doawloadFile)
                .result();
    }

    Future<Void> createMongoDb() {
        return Future.future((promise) -> {

            var query = new JsonObject().put("db_name", ConstantManager.Db_Name).put("connection_string", ConstantManager.Db_Uri);
            mongoClient = MongoClient.createShared(vertx, query);

            promise.complete();
        });
    }

    Future<Void> createIndex(Void unuse) {
        return Future.future((promise) -> {
            var jsonIndex = new JsonObject().put("_id", 1);
            mongoClient.createIndex(ConstanceUser.Collections_Driver, new JsonObject().put("wildcardProjection", jsonIndex), (asynRes) -> {
                if (asynRes.succeeded()) {
                    System.out.println(">> success create index");
                    promise.complete();
                    return;
                }

                asynRes.cause().printStackTrace();
                promise.fail("err create Index");
            });

        });
    }

    Future<Void> findOne(Void un) {
        return Future.future((promise) -> {
            var query = new JsonObject().put("_id", "639b0133b91e982d3a1f");
            mongoClient.findOne(ConstanceUser.Collections_Driver, query, null).onComplete((asynRes) -> {
                if(asynRes.succeeded()  ){
                    System.out.println(">> check res " + (asynRes.result() instanceof JsonObject));
                    promise.complete();
                    return;
                }
                promise.fail(asynRes.cause());
            });
        });
    }

    Future<Void> find(Void unuse) {
        return Future.future((promise) -> {
            FindOptions options = new FindOptions();
//            options.
            options.setSkip(1).setLimit(2);
            mongoClient.findWithOptions(ConstanceUser.Collections_Driver, new JsonObject(), options, (asynRes) -> {
                if (asynRes.succeeded()) {
                    System.out.println(">> list " + asynRes.result().toString());
                    promise.complete();
                    return;
                }
                asynRes.cause().printStackTrace();
                promise.complete();

            });
//            options.setCollation(new CollationOptions().setLocale("_id"));
//            mongoClient.find

        });
    }

    Future<Void> saveInfo(Void unuse) {
        return Future.future((promise) -> {
////            mongoClient.find
//            FindOptions option = new FindOptions();
//            option.
////option.
////option.setCollation(new CollationOptions().)
////mongoClient.fin

            var json = new JsonObject().put("test", "tungpham").put("override", "changer");
            mongoClient.save(ConstanceUser.Collections_Driver, json, asynRes -> {
                if (asynRes.succeeded()) {
                    System.out.println(">. success save " + asynRes.result());
                    promise.complete();
                    return;
                }

                asynRes.cause().printStackTrace();
                promise.fail("err save info ...");

            });

        });
    }

    Future<Void> createGridFs(Void unuse) {
        return Future.future((e) -> {
            mongoClient.createGridFsBucketService(ConstanceUser.dbGridFs, (asynRes) -> {
                if (asynRes.succeeded()) {
                    mongoGridFsClient = asynRes.result();
                    e.complete();
                } else {
                    e.fail("err create MongoGridFsClient");
                    e.complete();
                }

            });
        });
    }

    Future<Void> doawloadFile(Void unuse) {
        return Future.future((promoise) -> {
            mongoGridFsClient.downloadFile("test.er", (asyncResult) -> {
                if (asyncResult.succeeded()) {
                    long lent = asyncResult.result();
                    System.out.println(lent);
                }
                if (asyncResult.failed()) {
                    asyncResult.cause().printStackTrace();
                }
            });

//            mongoGridFs.downloadFileByID("test.er", "changer" ,(asyncResult) -> {
//                if(asyncResult.succeeded()){
//                    long lent = asyncResult.result();
//                    System.out.println(lent);
//                }
//                if(asyncResult.failed()){
//                    asyncResult.cause().printStackTrace();
//                }
//            });
            promoise.complete();
        });
    }

    Future<Void> uploadImage(Void unuse) {
        return Future.future((promise) -> {
            futFile().compose(aysnFile -> futUpload(aysnFile, mongoGridFsClient)).result();

//            mongoGridFs.uploadByFileName(asynFile, "test.er").onComplete((asynRes) -> {
//                if (asynRes.succeeded()) {
//                    System.out.println("success");
//                }
//                if (asynRes.failed()) {
//                    asynRes.cause().printStackTrace();
//                }
//            });
//            mongoGridFs.uploadFile("D:\\netbean12.0\\FaceBook\\DebtForgiveness\\Image\\buttonStartShare.png", res -> {
//                        if (res.succeeded()) {
//                            String id = res.result();
//                            //The ID of the stored object in Grid FS
//                        } else {
//                            res.cause().printStackTrace();
//                        }
//                    });
            promise.complete();
        });
    }

    Future<AsyncFile> futFile() {
        return vertx.fileSystem().open("D:\\netbean12.0\\ManagerRes\\company\\ManagerCar\\file-uploads\\d42d70d5-7d9d-470b-a3d2-b5c05dd99798.jpg", new OpenOptions());
    }

    Future<String> futUpload(AsyncFile asyncFile, MongoGridFsClient mongoGridFs) {
        return mongoGridFs.uploadByFileName(asyncFile, "checker.erxx");
    }

}
