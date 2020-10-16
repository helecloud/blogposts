resource "aws_secretsmanager_secret" "db-secret-rotation" {
  name        = "db-secret-app01"
  description = "This is an example secret"
  kms_key_id  = aws_kms_key.kms_key_db_secret.arn
  policy      = data.template_file.db_secret_policy.rendered

  rotation_lambda_arn = aws_lambda_function.db_autorotation.arn
  rotation_rules {
    automatically_after_days = 85
  }
}
