package lambda_native_demo;

import java.util.function.Supplier;
import javax.enterprise.context.ApplicationScoped;
import lambda_native_demo.service.SsmService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TokenRetriever implements Supplier<String> {
  private final SsmService ssmService;

  @ConfigProperty(name = "ssm.foobar-api-token-key")
  String ssmKeyName;

  public TokenRetriever(SsmService ssmService) {
    this.ssmService = ssmService;
  }

  @Override
  public String get() {
    return ssmService.getParameter(ssmKeyName, true);
  }
}
