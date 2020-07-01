                if re.search(r'\bCOMMANDSUCCESSFUL\b', ssm_run_command_stdout):
                    logger.info(
                        "setSecret: Secret rotation RunPowerShellScript has finished successfully on server %s" % db_instance_name)
                    publish_sns(sns_topic_arn, "db_rotate_secret: Lambda: RunPowerShellScript has finished successfully",
                                "Secret rotation RunPowerShellScript has finished successfully on server %s" % db_instance_name)
                else:
                    ssm_run_command_stderr = ssm_run_command_response['StandardErrorContent']
                    publish_sns(sns_topic_arn, "db_rotate_secret: Lambda: Secret rotations has failed",
                                "setSecret: RunPowerShellScript has failed with %s." % ssm_run_command_stderr)
                    service_client.update_secret_version_stage(SecretId=arn, VersionStage="AWSPENDING",
                                                               RemoveFromVersionId=token)
                    raise SystemExit("setSecret: RunPowerShellScript has failed with %s.." % ssm_run_command_stderr)
