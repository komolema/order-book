KC_USERNAME=orderbook-app-user
KC_PASSWORD=admin
KC_CLIENT=orderbook-service
KC_CLIENT_SECRET=M24r0YU6oyDGvfDtHFlioDrOvF0A69XD
KC_REALM=orderbook
KC_URL=http://localhost:9090
KC_RESPONSE=$(curl  -k \
        -d "username=$KC_USERNAME" \
        -d "password=$KC_PASSWORD" \
        -d 'grant_type=password' \
        -d "client_id=$KC_CLIENT" \
        -d "client_secret=$KC_CLIENT_SECRET" \
        "$KC_URL/realms/$KC_REALM/protocol/openid-connect/token" \
    | jq .)

KC_ACCESS_TOKEN=$(echo $KC_RESPONSE| jq -r .access_token)
echo $KC_ACCESS_TOKEN