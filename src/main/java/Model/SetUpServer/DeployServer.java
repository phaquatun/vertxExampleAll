package Model.SetUpServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class DeployServer extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        optionServer().result();// don't need
    }

    Future<String> optionServer() {
        int totalCore = Runtime.getRuntime().availableProcessors();
        System.out.println("total core " + totalCore);

        DeploymentOptions options = new DeploymentOptions()
                .setHa(true)
                .setWorkerPoolSize(50)
                .setWorker(true)
                .setInstances(totalCore*2)
                ;

        return vertx.deployVerticle(()->{
            return new SetUpServer();
        }, options);
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle("Model.SetUpServer.SetUpServer");
    }
}
