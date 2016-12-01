#!/usr/bin/env bash

# Allows you to wait until a services' TCP port is up
TIMEOUT=1
wait() {
    testServiceUp="(echo > /dev/tcp/localhost/$1) >/dev/null 2>&1 && echo 1 || echo 0"

    currentState=$(eval $testServiceUp)
    while [[ $currentState -lt 1 ]]
    do
      echo "$2 appears down, checking again in $TIMEOUT seconds"
      sleep $TIMEOUT
      currentState=$(eval $testDBUp)
    done
}

# Build the base docker for functional logging
pushd docker
pushd elastic
./docker-build.sh
popd
pushd filebeat
./docker-build.sh
popd
popd

# Build the log-test docker (based of functional-logging-base)
sbt docker:publishLocal

# kill all previously running docker containers
docker rm -f $(docker ps -aq)

# Start all docker containers
pushd docker
docker-compose up -d

# Wait for all docker containers to launch
wait 9300 "Elastic HTTP"
wait 9200 "Elastic Cluster port"
wait 5601 "Kibana"

open http://localhost:5601/

docker-compose logs -f
popd
