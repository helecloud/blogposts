| Environment variables                       |
|---------------------------------------------|
| export AWS_REGION=eu-west-1                 | 
| export LAMBDA_ROLE_ARN=<ARN>                |
| export FOOBAR_API_TOKEN_KEY=<SSM_PARAM_KEY> |
| export FOOBAR_API_URL=<FOOBAR_API>          |

**2. Run local**
mvn quarkus:dev
You can send test event e.g:
curl -d "{}" -X POST http://localhost:8080

**3. To deploy native via CLI:**
1. Prerequisites: lambda role with SSM, CloudWatch permissions, CloudWatch group, Parameter store secure string with token
2. mvn clean package -Dnative
3. Edit ./target/manage.sh (add --region param with desired AWS region in the create function)
4. Run ./target/manage.sh native create