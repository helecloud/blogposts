package lambda_native_demo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import lambda_native_demo.service.FoobarApiService;
import lambda_native_demo.service.SsmService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class LambdaHandlerTest {

  @Test
  public void testSimpleLambdaSuccess() throws Exception {
    // you test your lambas by invoking on http://localhost:8081
    // this works in dev mode too

    InputObject in = new InputObject();
    given()
        .contentType("application/json")
        .accept("application/json")
        .body(in)
        .when()
        .post()
        .then()
        .statusCode(200)
        .body(containsString("final result here"));
  }
}
