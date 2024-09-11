# OrderBook 

This is the readme for the orderbook app, in order to run the application you need to have the following software installed

- docker
- jq

The application is a Vert.X based web server that servers three APIs

- orderbook
- tradehistory
- limit

They are each protected by Keycloak and you need to get it running and you need to setup th authorization token

To start keycloak run the <b>./start-keycloak.sh</b> script which will start up keycloak with preconfigured realm. You may need to run it twice on your machine, make sure data directory exists in your source code top level directory.
Then you need to run <b>./get_token.sh</b> in order to get the token. Once you have obtained the token update the ACCESS_TOKEN in 
<b>http-client.env.json</b> file. To run sample rest endpoints calls. If you are using IntelliJ Ultimate you can run the tests.rest file.

To stop keycloak just run <b>./stop-keycloak.sh</b>

