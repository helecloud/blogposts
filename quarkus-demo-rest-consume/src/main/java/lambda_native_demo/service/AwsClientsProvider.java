package lambda_native_demo.service;

import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

@Singleton
public class AwsClientsProvider {
  @ConfigProperty(name = "aws.region")
  private String region;

  public SsmClient getSsmClient() {
    return SsmClient.builder()
        .region(Region.of(region))
        .httpClient(UrlConnectionHttpClient.builder().build())
        .build();
  }

}
