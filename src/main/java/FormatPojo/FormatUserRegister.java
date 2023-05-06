

package FormatPojo;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;



public class FormatUserRegister {
    
    JsonObject jsonReq ;
    RoutingContext ctx ;

    public FormatUserRegister(JsonObject jsonReq, RoutingContext ctx) {
        this.jsonReq = jsonReq;
        this.ctx = ctx;
    }

    public JsonObject getJsonReq() {
        return jsonReq;
    }

    public RoutingContext getCtx() {
        return ctx;
    }

    @Override
    public String toString() {
        return "FormatRegister{" + "jsonReq=" + jsonReq + ", ctx=" + ctx + '}';
    }
    
    
    
}
