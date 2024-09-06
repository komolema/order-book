package org.example.orderbook

import org.example.api.CurrencyPair
import org.example.api.Order
import java.util.*
import kotlin.collections.MutableMap

class OrderBookDB(private val matchingEngine: MatchingEngine) {
    private val buyOrders: MutableMap<CurrencyPair, PriorityQueue<Order>> = mutableMapOf()
    private val sellOrders: MutableMap<CurrencyPair, PriorityQueue<Order>> = mutableMapOf()

    fun addLimitBuyOrder(order: Order) {
        addOrder(order, buyOrders)
    }

    fun addLimitSellOrder(order: Order) {
        addOrder(order, sellOrders)
    }

    private fun addOrder(order: Order, ordersMap: MutableMap<CurrencyPair, PriorityQueue<Order>>) {
        ordersMap.computeIfAbsent(order.currencyPair) { PriorityQueue() }.add(order)
        processOrderMatching(order.currencyPair)
    }

    private fun processOrderMatching(currencyPair: CurrencyPair) {
        val buyOrder = buyOrders.computeIfAbsent(currencyPair) { PriorityQueue() }.poll()
        val sellOrder = sellOrders.computeIfAbsent(currencyPair) { PriorityQueue() }.poll()
        if (buyOrder != null && sellOrder != null) {
            when (matchingEngine.limitMatchOrder(buyOrder, sellOrder)) {
                is OrderMatchEvent.OrderFilled -> {}
                is OrderMatchEvent.PartialOrderFilled -> {}
                is OrderMatchEvent.OrderNotFilled -> {}
            }
        }
    }

    fun getBuyOrders(currencyPair: CurrencyPair): List<Order> =
        buyOrders[currencyPair]?.toList() ?: emptyList()

    fun getSellOrders(currencyPair: CurrencyPair): List<Order> =
        sellOrders[currencyPair]?.toList() ?: emptyList()
}