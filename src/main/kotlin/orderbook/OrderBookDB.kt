package org.example.orderbook

import arrow.core.Some
import org.example.api.CurrencyPair
import org.example.api.FilledOrder
import org.example.api.Order
import java.util.*
import kotlin.collections.MutableMap

class OrderBookDB(private val matchingEngine: MatchingEngine) {
    private val buyOrders: MutableMap<CurrencyPair, PriorityQueue<Order>> = mutableMapOf()
    private val sellOrders: MutableMap<CurrencyPair, PriorityQueue<Order>> = mutableMapOf()
    private val trades: MutableMap<CurrencyPair, MutableList<FilledOrder>> = mutableMapOf()

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
            val orderMatchEvent = matchingEngine.limitMatchOrder(buyOrder, sellOrder)
            when (orderMatchEvent) {
                is OrderMatchEvent.OrderFilled -> {
                    val filledOrder = FilledOrder(

                    )

                    val filledOrdersForCurrencyPair = trades.computeIfAbsent(currencyPair) { mutableListOf() }
                    filledOrdersForCurrencyPair.add(filledOrder)
                   trades[currencyPair] = filledOrdersForCurrencyPair

                    //remove the highest bids and asks
                    this.buyOrders.get(currencyPair)?.poll()
                    this.sellOrders.get(currencyPair)?.poll()
                }
                is OrderMatchEvent.PartialOrderFilled -> {
                    val filledOrder = FilledOrder(

                    )

                    val filledOrdersForCurrencyPair = trades.computeIfAbsent(currencyPair) { mutableListOf() }
                    filledOrdersForCurrencyPair.add(filledOrder)
                    trades[currencyPair] = filledOrdersForCurrencyPair

                    //remove either the highest bids or the highest asks
                    if(orderMatchEvent.partialBuyOrder is Some) {
                        this.buyOrders.get(currencyPair)?.poll()
                    }

                    if(orderMatchEvent.partialSellOrder is Some) {
                        this.sellOrders.get(currencyPair)?.poll()
                    }

                }
                is OrderMatchEvent.OrderNotFilled -> {}
            }
        }
    }

    fun getBuyOrders(currencyPair: CurrencyPair): List<Order> =
        buyOrders[currencyPair]?.toList() ?: emptyList()

    fun getSellOrders(currencyPair: CurrencyPair): List<Order> =
        sellOrders[currencyPair]?.toList() ?: emptyList()
}