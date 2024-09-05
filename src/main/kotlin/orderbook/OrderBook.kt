package org.example.orderbook


import org.example.api.Order
import java.util.*

class OrderBook(private val matchingEngine: MatchingEngine) {
    private val buyOrders = PriorityQueue<Order>()
    private val sellOrders = PriorityQueue<Order>()

    fun addLimitBuyOrder(order: Order) {
        buyOrders.add(order)
        this.matchOrder()
    }

    fun addLimitSellOrder(order: Order) {
        sellOrders.add(order)
        this.matchOrder()
    }

    private fun matchOrder() {
        val buyOrder = buyOrders.peek()
        val sellOrder = sellOrders.peek()

        val orderMatchEvent = this.matchingEngine.limitMatchOrder(buyOrder, sellOrder)

        when (orderMatchEvent) {
            is OrderMatchEvent.OrderFilled -> {}
            is OrderMatchEvent.PartialOrderFilled -> {}
            is OrderMatchEvent.OrderNotFilled -> {}
        }
    }

    fun getBuyOrders(): List<Order> = buyOrders.toList()
    fun getSellOrders(): List<Order> = sellOrders.toList()
}
