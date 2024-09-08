package org.example

import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.kotlin.ext.web.openapi.RouterBuilder
import org.example.api.CurrencyPair
import org.example.orderbook.OrderBookDB

class OrderBookApp(private val orderService: OrderService) : CoroutineVerticle() {
    override suspend fun start() {
        val router = Router.router(vertx)

        router.route(HttpMethod.GET, "/:currencyPair/orderbook").handler { ctx ->
            val currencyPair = ctx.pathParam("currencyPair")

            val orderBook = orderService.getOrderBook(CurrencyPair.fromString(currencyPair))

            val jsonOrderBook = Json.encode(orderBook)

            ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(jsonOrderBook)

        }

        router.route(HttpMethod.GET, "/:currencyPair/tradehistory").handler { ctx ->
            val currencyPair = ctx.pathParam("currencyPair")
            val filledOrders = orderService.tradeHistory(CurrencyPair.fromString(currencyPair))

            val jsonFilledOrders = Json.encode(filledOrders)

            ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(jsonFilledOrders)
        }

        router.route(HttpMethod.POST, "/v1/orders/limit").handler { ctx -> }

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8181)
            .coAwait()
    }

    override suspend fun stop() {}
}
