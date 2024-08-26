package org.example

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val vertx = Vertx.vertx()
    val server = vertx.createHttpServer()

    CoroutineScope(vertx.dispatcher())
        .launch {
            server.requestHandler {

            }
            awaitResult<Void> {
                server.listen(8080, it)
            }
            println("HTTP server started on port 8080")
        }

}