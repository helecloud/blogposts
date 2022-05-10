package lambda_native_demo.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@ApplicationScoped
public class SsmServiceImpl implements SsmService {
  @Inject
  AwsClientsProvider awsClientsProvider;
  @Override
  public String getParameter(final String key, final Boolean withDecrypt) {
    GetParameterRequest getParameterRequest =
        GetParameterRequest.builder().name(key).withDecryption(withDecrypt).build();

    GetParameterResponse parameter = awsClientsProvider.getSsmClient().getParameter(getParameterRequest);

    return parameter.parameter().value();
  }
}