# Description

This repository folder holds code snippets for the solution described in the blog post published on the AWS APN Portal```TBD``` in which we describe how to utilize AWS Secrets Manager and AWS Systems Manager Run Command to securely store and rotate database secrets.

## policies

* eks_get_secret_policy.json - IAM policy attached to EKS and DB servers
* secret_iam_user_policy.json - IAM policy attached to Secret administrators
* secret_kms_policy.json - Resource policy for the KMS key with which the secret is encrypted
* secret_rotation_lambda_policy.json - Rotation Lambda policy
* secrets_manager_policy.json - Resource policy for the Secret stored in the Secrets manager

## scripts

* rotate_secret_lambda_snippet_run_command.py - this lambda snippet is used to call Run Command upon secret rotation initiation from Secrets Manager
* rotate_db_password.ps1 - this powershell script is triggered from the lambda with Run Command upon secret rotation
* rotate_secret_lambda_snippet_parse_output.py - this lambda snippet is parsing the output from the PowerShell script and checks whether the rotation on the instance has been successful.

## terraform

* secret.tf - the terraform code which creates the secret and configures the rotation lambda
