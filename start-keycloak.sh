#!/bin/zsh

# Constants
CONTAINER_NAME="vertx-keycloak"
KEYCLOAK_VERSION="25.0.0"
KEYCLOAK_USER="admin"
KEYCLOAK_PASSWORD="admin"
KEYCLOAK_IMPORT_PATH="/tmp/vertx-realm.json"
HOST_IMPORT_PATH="$PWD/vertx-realm.json"
HOST_DATA_PATH="$PWD/data"
CONTAINER_DATA_PATH="/opt/jboss/keycloak/standalone/data"
PORT_MAPPING="8080:8080"
IMAGE="quay.io/keycloak/keycloak:$KEYCLOAK_VERSION"

# Docker Run Command
docker run \
  -it \
  --name $CONTAINER_NAME \
  --rm \
  -e KEYCLOAK_USER=$KEYCLOAK_USER \
  -e KEYCLOAK_PASSWORD=$KEYCLOAK_PASSWORD \
  -e KEYCLOAK_IMPORT=$KEYCLOAK_IMPORT_PATH \
  -v $HOST_IMPORT_PATH:$KEYCLOAK_IMPORT_PATH \
  -v $HOST_DATA_PATH:$CONTAINER_DATA_PATH \
  -p $PORT_MAPPING \
  $IMAGE