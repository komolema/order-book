package org.example

import org.example.api.CurrencyPair
import org.example.api.FilledOrder
import org.example.api.Order
import org.example.api.OrderBook
import org.example.orderbook.OrderBookDB

interface OrderService {

    fun addLimitOrder(order: Order)
    fun getOrderBook(currencyPair: CurrencyPair): OrderBook
    fun tradeHistory(currencyPair: CurrencyPair): List<FilledOrder>
}

class OrderServiceImpl(val orderBookDB: OrderBookDB) : OrderService {
    override fun addLimitOrder(order: Order) {
        this.orderBookDB.addLimitBuyOrder(order)
    }

    override fun getOrderBook(currencyPair: CurrencyPair): OrderBook {
        val asks = this.orderBookDB.getBuyOrders(currencyPair)
        val bids = this.orderBookDB.getSellOrders(currencyPair)
        return OrderBook(
            asks = asks,
            bids = bids
        )
    }

    override fun tradeHistory(currencyPair: CurrencyPair): List<FilledOrder> {
        return this.orderBookDB.getTrades(currencyPair)
    }
}