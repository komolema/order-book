package org.example

import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.kotlin.ext.web.openapi.RouterBuilder
import org.example.orderbook.OrderBookDB

class OrderBookApp(val orderBookDB: OrderBookDB): CoroutineVerticle() {
    override suspend fun start() {
        val router = Router.router(vertx)

        router.route(HttpMethod.GET, "/marketdata/:currencyPair/orderbook").handler { ctx ->
            val currencyPair = ctx.pathParam("currencyPair")

        }

        router.route(HttpMethod.POST, "/v1/orders/limit").handler { ctx ->}

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8181)
            .coAwait()
    }
    override suspend fun stop() {}
}
