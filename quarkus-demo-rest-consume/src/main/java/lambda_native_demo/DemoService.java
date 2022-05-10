package lambda_native_demo;

import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lambda_native_demo.model.FoobarResponse;
import lambda_native_demo.service.FoobarApiService;
import lambda_native_demo.service.SsmService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DemoService {
  private static final Logger log = Logger.getLogger(DemoService.class);
  @Inject
  private TokenRetriever tokenRetriever;

  @Inject @RestClient
  FoobarApiService foobarApiService;

  public OutputObject process(Map<String, String> event) {
    final String token = tokenRetriever.get();
    FoobarResponse foobarResponse = foobarApiService.searchDemoResource(token, "term");
    log.info("Search response: " + foobarResponse.getSuccess());

    // some processing etc

    return buildOutput();
  }

  private OutputObject buildOutput() {
    OutputObject outputObject = new OutputObject();
    outputObject.setResult("final result here");

    return outputObject;
  }
}
