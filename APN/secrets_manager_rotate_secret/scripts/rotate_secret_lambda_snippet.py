ssm_run_command = ssm_client.send_command(
    Targets=[{'Key': "tag:Name", 'Values': [db_instance_name]}],
    DocumentName='AWS-RunPowerShellScript',
    Comment=f'Change database password update on server {db_instance_name}.',
    Parameters={'commands': [
        f'$env:DBInstanceName = "{db_instance_name}"',
        f'$env:SecretId = "{arn}"',
        f'$env:SecretName = "{secret_name}"',
        f'$env:LoginName = "{login_name}"',
        f'Start-Process powershell "c:/SQLInstall/rotate_db_password.ps1" -NoNewWindow'
    ]},
    CloudWatchOutputConfig={
        'CloudWatchLogGroupName': f'/sqlserver/secretsmanager',
        'CloudWatchOutputEnabled': True
    }
)
