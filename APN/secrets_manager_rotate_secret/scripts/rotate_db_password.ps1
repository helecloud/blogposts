       $password = (Get-SECSecretValue -SecretId $env:SecretId  -Select SecretString -VersionStage "AWSPENDING")
        $srv = New-Object "Microsoft.SqlServer.Management.Smo.Server" $ServerName

        if (!$password)
        {
            Throw "Exiting. We were not able to get the password from AWS Secret Manager"
        }
        else
        {
            if ($srv.Logins.Contains($loginName))
            {
                Write-Host -Object "User '$loginName' found. Changing password..."
                $SQLUser = $srv.Logins | where {$_.Name -eq "$loginName"}
                $SQLUser.ChangePassword($password);
                $SQLUser.Alter();
                $SQLUser.Refresh();
            }else{
                Write-Host -Object "Create new sql user '$loginName'"
                $login = New-Object -TypeName Microsoft.SqlServer.Management.Smo.Login -ArgumentList $ServerName, $loginName
                $login.LoginType = [Microsoft.SqlServer.Management.Smo.LoginType]::SqlLogin
                $login.Create($password)
            }
        Invoke-Sqlcmd -ServerInstance $ServerName -Username $loginName -Password $password -Query "select @@version" | Out-Null
        Write-Host -Object "PASSWORDUPDATESUCCESSFUL"
