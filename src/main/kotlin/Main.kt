package org.example

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun main() {
    val vertx = Vertx.vertx()
    val server = vertx.createHttpServer()
    val scope = CoroutineScope(vertx.dispatcher())

    scope.launch {
        try {
            server.requestHandler { req ->
                req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!")
            }
            awaitResult<Void> {
                server.listen(8080)
            }
            println("HTTP server started on port 8080")
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Failed to start HTTP server: ${ex.message}")
        } finally {
            vertx.close()
        }
    }
}