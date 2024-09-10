# Use the official Keycloak image as the base
FROM quay.io/keycloak/keycloak:latest

# Set environment variables for Keycloak configuration
ENV KEYCLOAK_USER=admin
ENV KEYCLOAK_PASSWORD=admin
ENV KC_DB=dev-mem

# Install required dependencies for running Keycloak
RUN microdnf install findutils

# Change the working directory to the Keycloak directory
WORKDIR /opt/keycloak

# Initialize Keycloak and specify the H2 in-memory database
RUN /opt/keycloak/bin/kc.sh build

# Expose Keycloak's default port
EXPOSE 8080

# Command to start Keycloak using H2 in-memory database
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]