#!/bin/zsh

# Constants
CONTAINER_NAME="vertx-keycloak"
KEYCLOAK_VERSION="25.0.5"
KEYCLOAK_USER="admin"
KEYCLOAK_PASSWORD="admin"
KEYCLOAK_IMPORT_PATH="/opt/keycloak/data/import/orderbook-realm.json"
HOST_IMPORT_PATH="$PWD/orderbook-realm.json"
HOST_DATA_PATH="$PWD/data"
CONTAINER_DATA_PATH="/opt/keycloak/data"
PORT_MAPPING="9090:8080"
IMAGE="quay.io/keycloak/keycloak:$KEYCLOAK_VERSION"

# Docker Run Command
docker run \
  -it \
  --name $CONTAINER_NAME \
  --rm \
  -e KEYCLOAK_ADMIN=$KEYCLOAK_USER \
  -e KEYCLOAK_ADMIN_PASSWORD=$KEYCLOAK_PASSWORD \
  -e KEYCLOAK_IMPORT=$KEYCLOAK_IMPORT_PATH \
  -v $HOST_IMPORT_PATH:$KEYCLOAK_IMPORT_PATH \
  -v $HOST_DATA_PATH:$CONTAINER_DATA_PATH \
  -p $PORT_MAPPING \
  $IMAGE \
  start-dev --import-realm --verbose