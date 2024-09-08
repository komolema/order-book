package org.example.orderbook

import arrow.core.Either
import arrow.core.Some
import arrow.core.left
import arrow.core.right
import org.example.api.CurrencyPair
import org.example.api.FilledOrder
import org.example.api.Order
import org.example.api.OrderSide
import java.util.*
import kotlin.collections.MutableMap

class OrderBookDB(private val matchingEngine: MatchingEngine) {
    private val buyOrders: MutableMap<CurrencyPair, PriorityQueue<Order>> = mutableMapOf()
    private val sellOrders: MutableMap<CurrencyPair, PriorityQueue<Order>> = mutableMapOf()
    private val trades: MutableMap<CurrencyPair, MutableList<FilledOrder>> = mutableMapOf()
    private val tradeVolume: MutableMap<CurrencyPair, Double> = mutableMapOf()
    private var sequenceId: Long = 0

    private fun incrementSequenceId(): Long = sequenceId++

    private fun calculateTradeVolume(currencyPair: CurrencyPair, quantity: Double): Double {
        tradeVolume.compute(currencyPair) { _, currentVolume ->
            (currentVolume ?: 0.0) + quantity
        }
        return tradeVolume[currencyPair]!!
    }

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
            when (val orderMatchEvent = matchingEngine.limitMatchOrder(buyOrder, sellOrder)) {
                is OrderMatchEvent.OrderFilled -> {
                    val filledBuyOrder = orderMatchEvent.buyOrder
                    val filledOrder = FilledOrder(
                        takerSide = OrderSide.BUY,
                        quantity = filledBuyOrder.quantity,
                        price = filledBuyOrder.price,
                        currencyPair = filledBuyOrder.currencyPair,
                        tradedAt = orderMatchEvent.fillTime,
                        quoteVolume = calculateTradeVolume(
                            filledBuyOrder.currencyPair,
                            filledBuyOrder.price * filledBuyOrder.quantity
                        ),
                        sequenceId = incrementSequenceId()
                    )

                    val filledOrdersForCurrencyPair = trades.computeIfAbsent(currencyPair) { mutableListOf() }
                    filledOrdersForCurrencyPair.add(filledOrder)
                    trades[currencyPair] = filledOrdersForCurrencyPair

                    //remove the highest bids and asks
                    this.buyOrders[currencyPair]?.poll()
                    this.sellOrders[currencyPair]?.poll()
                }

                is OrderMatchEvent.PartialOrderFilled -> {
                    val filledOrderResult =
                        generateFilledOrderAndRemoveBuyOrSellSideOrder(orderMatchEvent, currencyPair)

                    when (filledOrderResult) {
                        is Either.Right -> {
                            filledOrderResult.value.let { filledOrderAndPartialOrder ->
                                {
                                    val filledOrdersForCurrencyPair =
                                        trades.computeIfAbsent(currencyPair) { mutableListOf() }
                                    filledOrdersForCurrencyPair.add(filledOrderAndPartialOrder.filledOrder)
                                    trades[currencyPair] = filledOrdersForCurrencyPair
                                }
                            }
                        }
                        is Either.Left -> println("Error: ${filledOrderResult.value}")
                    }
                }

                is OrderMatchEvent.OrderNotFilled -> {}
            }
        }
    }

    private fun generateFilledOrderAndRemoveBuyOrSellSideOrder(
        orderMatchEvent: OrderMatchEvent.PartialOrderFilled,
        currencyPair: CurrencyPair
    ): Either<String, FilledOrderAndPartialOrder> {
        val filledBuyOrder = orderMatchEvent.partialBuyOrder.getOrNull()!!
        val filledSellOrder = orderMatchEvent.partialSellOrder.getOrNull()!!
        val fOrder = orderMatchEvent.filledOrder

        if (orderMatchEvent.partialBuyOrder is Some) {
            val filledOrder = FilledOrder(
                takerSide = OrderSide.SELL,
                quantity = fOrder.sellOrder.quantity,
                price = fOrder.sellOrder.price,
                currencyPair = fOrder.sellOrder.currencyPair,
                tradedAt = fOrder.fillTime,
                quoteVolume = calculateTradeVolume(
                    fOrder.sellOrder.currencyPair,
                    fOrder.sellOrder.price * fOrder.sellOrder.quantity
                ),
                sequenceId = incrementSequenceId()
            )
            this.buyOrders[currencyPair]?.poll()
            this.sellOrders[currencyPair]?.poll()

            addLimitBuyOrder(filledBuyOrder)

            val filledOrderAndPartialOrder = FilledOrderAndPartialOrder(filledOrder, filledBuyOrder)
            return (filledOrderAndPartialOrder).right()
        }

        if (orderMatchEvent.partialSellOrder is Some) {
            val filledOrder = FilledOrder(
                takerSide = OrderSide.BUY,
                quantity = fOrder.buyOrder.quantity,
                price = fOrder.buyOrder.price,
                currencyPair = fOrder.buyOrder.currencyPair,
                tradedAt = fOrder.fillTime,
                quoteVolume = calculateTradeVolume(
                    fOrder.buyOrder.currencyPair,
                    fOrder.buyOrder.price * fOrder.buyOrder.quantity
                ),
                sequenceId = incrementSequenceId()
            )

            this.buyOrders[currencyPair]?.poll()
            this.sellOrders[currencyPair]?.poll()

            addLimitSellOrder(filledSellOrder)

            val filledOrderAndPartialOrder = FilledOrderAndPartialOrder(filledOrder, filledSellOrder)
            return (filledOrderAndPartialOrder).right()
        }
        val errorString =
            "There supplied buy($filledBuyOrder) or sell($filledSellOrder) orders are none, please provide values with at least one order"
        return errorString.left()
    }

    fun getBuyOrders(currencyPair: CurrencyPair): List<Order> =
        buyOrders[currencyPair]?.toList() ?: emptyList()

    fun getSellOrders(currencyPair: CurrencyPair): List<Order> =
        sellOrders[currencyPair]?.toList() ?: emptyList()

    fun getTrades(currencyPair: CurrencyPair): List<FilledOrder> =
        trades[currencyPair]?.toList() ?: emptyList()
}

data class FilledOrderAndPartialOrder( val filledOrder: FilledOrder, val partialOrder: Order)