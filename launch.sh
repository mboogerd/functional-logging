#!/usr/bin/env bash


# === METHODS ===

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

requestDockerBuild() {
    read -p "Would you like to build $1: [n]" -n 1 -r
    if [[ $REPLY =~ ^[Yy]$ ]]
    then
        ./docker-build.sh
    fi
}

pushd () {
    command pushd "$@" > /dev/null
}

popd () {
    command popd "$@" > /dev/null
}

# === Script execution ===

# Build the base docker for functional logging
pushd docker
pushd elastic
requestDockerBuild elastic-xpack
popd
pushd kibana
requestDockerBuild kibana-xpack
popd
pushd filebeat
requestDockerBuild "functional-logging base image"
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
