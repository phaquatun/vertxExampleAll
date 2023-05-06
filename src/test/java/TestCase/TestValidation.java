package TestCase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Parameters;
import static io.vertx.ext.web.validation.builder.Parameters.param;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.json.schema.common.Validator;
import static io.vertx.json.schema.common.dsl.Schemas.intSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;
import static io.vertx.json.schema.draft7.dsl.Keywords.maximum;

public class TestValidation extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        SchemaParser schema = SchemaParser.createDraft7SchemaParser(SchemaRouter.create(vertx, new SchemaRouterOptions()));
//        ValidationHandler validationHandler = ValidationHandler.builder(schema)
//                .queryParameter(param("", intSchema()));
    }

}
