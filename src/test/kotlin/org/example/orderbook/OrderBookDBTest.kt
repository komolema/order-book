package org.example.orderbook

import arrow.core.None
import arrow.core.Some
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.example.api.CurrencyPair
import org.example.api.FilledOrder
import org.example.api.Order
import org.example.api.OrderSide
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class OrderBookDBTest {
    private lateinit var orderBookDB: OrderBookDB
    private lateinit var matchingEngine: MatchingEngine

    @BeforeEach
    fun setup() {
        matchingEngine = mock()
        orderBookDB = OrderBookDB(matchingEngine)
    }

    @Test
    fun `test process order matching with OrderFilled event`() {
        val buyOrder = Order(UUID.randomUUID().toString(), OrderSide.BUY, 100.0, 120.0, CurrencyPair.BTCZAR)
        val sellOrder = Order(UUID.randomUUID().toString(), OrderSide.SELL, 100.0, 120.0, CurrencyPair.BTCZAR)
        whenever(matchingEngine.limitMatchOrder(buyOrder, sellOrder))
            .thenReturn(OrderMatchEvent.OrderFilled(buyOrder, sellOrder, Clock.System.now()))

        orderBookDB.addLimitBuyOrder(buyOrder)
        orderBookDB.addLimitSellOrder(sellOrder)

        val buyOrders = orderBookDB.getBuyOrders(CurrencyPair.BTCZAR)
        val sellOrders = orderBookDB.getSellOrders(CurrencyPair.BTCZAR)
        val trades = orderBookDB.getTrades(CurrencyPair.BTCZAR)

        assertTrue(buyOrders.isEmpty(), "Buy orders should be empty after full order match")
        assertTrue(sellOrders.isEmpty(), "Sell orders should be empty after full order match")
        assertEquals(1, trades.size, "There should be a trade after full order match")
    }

    @Test
    fun `test process order matching with PartialOrderFilled event`() {
        // Create the buy order
        val id = UUID.randomUUID().toString()
        val buyOrder = Order(id, OrderSide.BUY, 200.0, 120.0, CurrencyPair.BTCZAR)
        val partialBuyOrder = Order(id, OrderSide.BUY, 100.0, 120.0, CurrencyPair.BTCZAR)
        // Create the sell order
        val sellOrder = Order(UUID.randomUUID().toString(), OrderSide.SELL, 100.0, 120.0, CurrencyPair.BTCZAR)

        // Convert sellOrder to OrderMatchEvent.OrderFilled type
        val filledSellOrder = OrderMatchEvent.OrderFilled(
            buyOrder = buyOrder,
            sellOrder = sellOrder
        )

        // Mock the limitMatchOrder to return PartialOrderFilled event
        whenever(matchingEngine.limitMatchOrder(buyOrder, sellOrder))
            .thenReturn(
                OrderMatchEvent.PartialOrderFilled(
                    filledSellOrder,
                    Some(partialBuyOrder),
                    None,
                    Clock.System.now()
                )
            )

        // Add orders to the order book
        orderBookDB.addLimitBuyOrder(buyOrder)
        orderBookDB.addLimitSellOrder(sellOrder)

        // Retrieve orders and trades from the order book
        val buyOrders = orderBookDB.getBuyOrders(CurrencyPair.BTCZAR)
        val sellOrders = orderBookDB.getSellOrders(CurrencyPair.BTCZAR)
        val trades = orderBookDB.getTrades(CurrencyPair.BTCZAR)

        // Assertions
        assertEquals(1, buyOrders.size, "Buy orders should be empty after partial order match")
        assertTrue(sellOrders.isEmpty(), "Sell orders should be empty after partial order match")
        assertEquals(1, trades.size, "There should be a trade after partial order match")
    }

    @Test
    fun `test process order matching with PartialOrderFilled event for sell side`() {
        // Create the buy order
        val id = UUID.randomUUID().toString()
        val buyOrder = Order(UUID.randomUUID().toString(), OrderSide.BUY, 100.0, 120.0, CurrencyPair.BTCZAR)

        // Create the sell order
        val sellOrder = Order(id, OrderSide.SELL, 300.0, 120.0, CurrencyPair.BTCZAR)
        val partialSellOrder = Order(id, OrderSide.SELL, 200.0, 120.0, CurrencyPair.BTCZAR)

        // Convert sellOrder to OrderMatchEvent.OrderFilled type
        val filledSellOrder = OrderMatchEvent.OrderFilled(
            buyOrder = buyOrder,
            sellOrder = sellOrder
        )

        // Mock the limitMatchOrder to return PartialOrderFilled event
        whenever(matchingEngine.limitMatchOrder(buyOrder, sellOrder))
            .thenReturn(
                OrderMatchEvent.PartialOrderFilled(
                    filledSellOrder,
                    None,
                    Some(partialSellOrder),
                    Clock.System.now()
                )
            )

        // Add orders to the order book
        orderBookDB.addLimitBuyOrder(buyOrder)
        orderBookDB.addLimitSellOrder(sellOrder)

        // Retrieve orders and trades from the order book
        val buyOrders = orderBookDB.getBuyOrders(CurrencyPair.BTCZAR)
        val sellOrders = orderBookDB.getSellOrders(CurrencyPair.BTCZAR)
        val trades = orderBookDB.getTrades(CurrencyPair.BTCZAR)

        // Assertions
        assertTrue(buyOrders.isEmpty(), "Buy orders should be empty after partial order match")
        assertEquals(1, sellOrders.size, "Sell orders should be empty after partial order match")
        assertEquals(1, trades.size, "There should be a trade after partial order match")
    }

    @Test
    fun `test process order matching with OrderNotFilled event`() {
        val buyOrder = Order(UUID.randomUUID().toString(), OrderSide.BUY, 100.0, 120.0, CurrencyPair.BTCZAR)
        val sellOrder = Order(UUID.randomUUID().toString(), OrderSide.SELL, 200.0, 120.0, CurrencyPair.BTCZAR)
        whenever(matchingEngine.limitMatchOrder(buyOrder, sellOrder))
            .thenReturn(OrderMatchEvent.OrderNotFilled())

        orderBookDB.addLimitBuyOrder(buyOrder)
        orderBookDB.addLimitSellOrder(sellOrder)

        val buyOrders = orderBookDB.getBuyOrders(CurrencyPair.BTCZAR)
        val sellOrders = orderBookDB.getSellOrders(CurrencyPair.BTCZAR)
        val trades = orderBookDB.getTrades(CurrencyPair.BTCZAR)

        assertTrue(buyOrders.isNotEmpty(), "Buy orders should not be empty after no order match")
        assertTrue(sellOrders.isNotEmpty(), "Sell orders should not be empty after no order match")
        assertTrue(trades.isEmpty(), "There shouldn't be any trades after no order match")
    }
}