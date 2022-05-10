package lambda_native_demo.service;

public interface SsmService {

  String getParameter(String key, Boolean withDecrypt);
}
