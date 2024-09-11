#!/bin/zsh

# Constants
CONTAINER_NAME="vertx-keycloak"

# Stop the container
echo "Stopping Keycloak container: $CONTAINER_NAME..."
docker stop $CONTAINER_NAME

# Remove the container
echo "Removing Keycloak container: $CONTAINER_NAME..."
docker rm $CONTAINER_NAME