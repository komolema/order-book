package org.example.orderbook

import org.example.api.Order
import java.util.PriorityQueue


import java.util.TreeMap

class OrderBook(private val matchingEngine: MatchingEngine) {
    private val buyOrders = PriorityQueue<Order>()
    private val sellOrders = PriorityQueue<Order>()

    fun addBuyOrder(order: Order) {
        buyOrders.add(order)
        this.matchOrder()
    }

    fun addSellOrder(order: Order) {
        sellOrders.add(order)
        this.matchOrder()
    }

    private fun matchOrder() {
        val buyOrder = buyOrders.peek()
        val sellOrder = sellOrders.peek()

        val orderMatchEvent = this.matchingEngine.limitMatchOrder(buyOrder, sellOrder)
    }

    fun getBuyOrders(): PriorityQueue<Order> = buyOrders
    fun getSellOrders(): PriorityQueue<Order> = sellOrders
}
