package lambda_native_demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

@Named("demo")
public class DemoLambda implements RequestHandler<Map<String, String>, OutputObject> {

  @Inject
  DemoService demoService;

  @Override
  public OutputObject handleRequest(Map<String, String> event, Context context) {

    OutputObject outputObject = demoService.process(event);
    return outputObject.setRequestId(context.getAwsRequestId());
  }
}
