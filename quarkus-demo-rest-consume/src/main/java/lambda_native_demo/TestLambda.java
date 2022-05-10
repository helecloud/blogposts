package lambda_native_demo;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

@Named("test")
public class TestLambda implements RequestHandler<Map<String, String>, OutputObject> {

    @Inject
    DemoService service;

    @Override
    public OutputObject handleRequest(Map<String, String> event, Context context) {
        return service.process(event).setRequestId(context.getAwsRequestId());
    }
}
