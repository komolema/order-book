package org.example.orderbook

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.example.api.Order

interface MatchingEngine {
    fun limitMatchOrder(buyOrder: Order, sellOrder: Order): OrderMatchEvent
}

sealed class OrderMatchEvent {
    data class OrderNotFilled(val matchTime: Instant = Clock.System.now()) : OrderMatchEvent()
    data class OrderFilled(
        val buyOrder: Order,
        val sellOrder: Order,
        val fillTime: Instant = Clock.System.now()
    ) : OrderMatchEvent()

    data class PartialOrderFilled(
        val filledOrder: OrderFilled,
        val partialBuyOrder: Option<Order>,
        val partialSellOrder: Option<Order>,
        val fillTime: Instant = Clock.System.now()
    ) : OrderMatchEvent()
}

class MatchingEngineImpl : MatchingEngine {
    override fun limitMatchOrder(buyOrder: Order, sellOrder: Order): OrderMatchEvent {

        if (buyOrder.price >= sellOrder.price) {
            if (buyOrder.quantity == sellOrder.quantity) {
                return OrderMatchEvent.OrderFilled(
                    buyOrder = buyOrder,
                    sellOrder = sellOrder,
                )
            } else if(buyOrder.quantity < sellOrder.quantity) {

                val partialSellOrder = sellOrder.copy(quantity = sellOrder.quantity - buyOrder.quantity)
                //Here we need to
                return OrderMatchEvent.PartialOrderFilled(
                    filledOrder = OrderMatchEvent.OrderFilled(
                        buyOrder = buyOrder,
                        sellOrder = sellOrder
                    ),
                    partialBuyOrder = None,
                    partialSellOrder = Some(partialSellOrder)
                )
            } else {
                val partialBuyOrder = buyOrder.copy(quantity = buyOrder.quantity - sellOrder.quantity)
                return OrderMatchEvent.PartialOrderFilled(
                    filledOrder = OrderMatchEvent.OrderFilled(
                        buyOrder = buyOrder,
                        sellOrder = sellOrder,
                    ),
                    partialBuyOrder = Some(partialBuyOrder),
                    partialSellOrder = None,
                )
            }
        }
        return OrderMatchEvent.OrderNotFilled()
    }
}

