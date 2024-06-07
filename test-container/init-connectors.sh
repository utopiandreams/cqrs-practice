#!/bin/bash

# Start the Connect service in the background
/kafka/bin/connect-distributed.sh kafka/config.orig/connect-distributed.properties &

# debezium connect 가 준비될 때까지 기다립니다.
while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' http://localhost:8083/connectors)" != "200" ]]; do
  echo "Waiting for Kafka Connect to start..."
  sleep 5
done

# mysql connector 를 등록합니다.
curl -X POST -H "Content-Type: application/json" --data @/kafka-connectors/mysql-connector.json http://localhost:8083/connectors

wait

