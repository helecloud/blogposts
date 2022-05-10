package lambda_native_demo.model;

import lombok.Data;

@Data
public class SearchRequest {
  private String term;

  private Boolean exactMatch;
}
