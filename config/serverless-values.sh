#!/usr/bin/env bash

configFile() {
  key=$1
  jsonKey=$2

  echo "$key: $(cat config/output.json | jq .$jsonKey | tr -d '"')" >> config/config.yml
}

databaseUrlParameter() {
  jsonKey=$1

  echo "$(cat config/output.json | jq .$jsonKey.value | tr -d '"')"
}

rm config/config.yml

echo "mysqlUrl: jdbc:mysql://$(databaseUrlParameter 'database_endpoint'):$(databaseUrlParameter 'database_port')/$(databaseUrlParameter database_name)" >> config/config.yml
configFile "mysqlUser" "database_username.value"
configFile "mysqlPassword" "database_password.value"

configFile "securityGroup" "serverless_security_group.value"

configFile "subnetOne" "lambda_subnet_ids.value[0]"
configFile "subnetTwo" "lambda_subnet_ids.value[1]"
configFile "subnetThree" "lambda_subnet_ids.value[2]"
