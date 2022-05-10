package lambda_native_demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoobarResponse {
  private Boolean success;

  private FoobarData data;
}
