package org.example

import io.vertx.core.Context
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.auth.oauth2.OAuth2Auth
import io.vertx.ext.auth.oauth2.OAuth2Options
import io.vertx.ext.auth.oauth2.impl.OAuth2AuthProviderImpl
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.OAuth2AuthHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.kotlin.ext.auth.oauth2.providers.KeycloakAuth
import io.vertx.kotlin.ext.web.openapi.RouterBuilder
import org.example.api.CurrencyPair
import org.example.api.Order
import org.example.orderbook.OrderBookDB


class OrderBookApp(private val orderService: OrderService) : CoroutineVerticle() {


    override suspend fun start() {

        val oauth2Options = OAuth2Options().apply {
            clientId = "orderbook-app"
            site = "http://localhost:9090/realms/orderbook"
            clientSecret = "12345"
        }

        val oauth2Provider: OAuth2Auth = KeycloakAuth.discoverAwait(vertx, oauth2Options)

        val oauth2Handler = OAuth2AuthHandler.create(vertx, oauth2Provider)


        val router = Router.router(vertx)

        router.route().handler(BodyHandler.create())
        router.route("/api/*").handler(oauth2Handler)

        router.route(HttpMethod.GET, "/api/v1/:currencyPair/orderbook").handler { ctx ->
            checkUser(ctx)

            val currencyPair = ctx.pathParam("currencyPair")

            val orderBook = orderService.getOrderBook(CurrencyPair.fromString(currencyPair))

            val jsonOrderBook = Json.encode(orderBook)

            ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(jsonOrderBook)

        }

        router.route(HttpMethod.GET, "/api/v1/:currencyPair/tradehistory").handler { ctx ->
            checkUser(ctx)

            val currencyPair = ctx.pathParam("currencyPair")
            val filledOrders = orderService.tradeHistory(CurrencyPair.fromString(currencyPair))

            val jsonFilledOrders = Json.encode(filledOrders)

            ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(jsonFilledOrders)
        }

        router.route(HttpMethod.POST, "/api/v1/orders/limit").handler { ctx ->
            checkUser(ctx)
            val bodyAsString = ctx.body().asString()

            if(bodyAsString.isNullOrEmpty()){
                ctx.response().setStatusCode(400).end("Request body is empty")
            } else {
                try {
                    val order: Order = Json.decodeValue(bodyAsString, Order::class.java)

                    orderService.addLimitOrder(order)

                    ctx.response().setStatusCode(201).end()
                } catch (e: Exception) {
                    ctx.response().setStatusCode(400).end("Invalid order: ${e.message}")
                }
            }
        }

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8282)
            .coAwait()
    }

    override suspend fun stop() {}

    private fun checkUser(ctx: RoutingContext) {
        val user = ctx.user()
        if (user == null) {
            ctx.response().setStatusCode(401).end("Unauthorized")
        }
    }
}
