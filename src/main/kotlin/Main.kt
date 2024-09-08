package org.example

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.orderbook.MatchingEngine
import org.example.orderbook.MatchingEngineImpl
import org.example.orderbook.OrderBookDB

suspend fun main() {

    val matchingEngine = MatchingEngineImpl()
    val orderBookDB = OrderBookDB(matchingEngine)
    val orderService = OrderServiceImpl(orderBookDB)

    val vertx = Vertx.vertx()
    vertx.deployVerticle(OrderBookApp(orderService)).await()

}