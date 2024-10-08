package org.example.orderbook

import org.example.api.Order
import org.example.api.OrderSide
import org.example.api.CurrencyPair
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MatchingEngineImplTest {
    private val matchingEngine = MatchingEngineImpl()

    @Test
    fun `fill order when buy price is equal to sell price and quantities are the same`() {
        val buyOrder = Order(
            side = OrderSide.BUY, quantity = 1000.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)
        val sellOrder = Order(side = OrderSide.SELL, quantity = 1000.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)

        val orderMatchResult = matchingEngine.limitMatchOrder(buyOrder, sellOrder)

        assertTrue(orderMatchResult is OrderMatchEvent.OrderFilled)
        assertEquals(buyOrder.id, (orderMatchResult as OrderMatchEvent.OrderFilled).buyOrder.id)
        assertEquals(sellOrder.id, (orderMatchResult as OrderMatchEvent.OrderFilled).sellOrder.id)
    }

    @Test
    fun `fill order when buy price is higher to sell price and quantities are the same`() {
        val buyOrder = Order(
            side = OrderSide.BUY, quantity = 1000.00, price = 1.5, currencyPair = CurrencyPair.BTCZAR)
        val sellOrder = Order(side = OrderSide.SELL, quantity = 1000.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)

        val orderMatchResult = matchingEngine.limitMatchOrder(buyOrder, sellOrder)

        assertTrue(orderMatchResult is OrderMatchEvent.OrderFilled)
        assertEquals(buyOrder.id, (orderMatchResult as OrderMatchEvent.OrderFilled).buyOrder.id)
        assertEquals(sellOrder.id, (orderMatchResult as OrderMatchEvent.OrderFilled).sellOrder.id)
    }

    @Test
    fun `partial fill when buy order quantity is less than sell order quantity`() {
        val buyOrder = Order(side = OrderSide.BUY, quantity = 500.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)
        val sellOrder = Order(side = OrderSide.SELL, quantity = 1000.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)

        val orderMatchResult = matchingEngine.limitMatchOrder(buyOrder, sellOrder)

        assertTrue(orderMatchResult is OrderMatchEvent.PartialOrderFilled)
        assertNull((orderMatchResult as OrderMatchEvent.PartialOrderFilled).partialBuyOrder.orNull())
        assertNotNull((orderMatchResult).partialSellOrder.orNull())
        assertEquals(500.00, (orderMatchResult).partialSellOrder.orNull()?.quantity)
    }

    @Test
    fun `partial fill when buy order quantity is less than sell order quantity and price is higher`() {
        val buyOrder = Order(side = OrderSide.BUY, quantity = 500.00, price = 3.0, currencyPair = CurrencyPair.BTCZAR)
        val sellOrder = Order(side = OrderSide.SELL, quantity = 1000.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)

        val orderMatchResult = matchingEngine.limitMatchOrder(buyOrder, sellOrder)

        assertTrue(orderMatchResult is OrderMatchEvent.PartialOrderFilled)
        assertNull((orderMatchResult as OrderMatchEvent.PartialOrderFilled).partialBuyOrder.orNull())
        assertNotNull((orderMatchResult).partialSellOrder.orNull())
        assertEquals(500.00, (orderMatchResult).partialSellOrder.orNull()?.quantity)
    }

    @Test
    fun `partial fill when sell order quantity is less than buy order quantity`() {
        val buyOrder = Order(side = OrderSide.BUY, quantity = 1000.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)
        val sellOrder = Order(side = OrderSide.SELL, quantity = 500.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)

        val orderMatchResult = matchingEngine.limitMatchOrder(buyOrder, sellOrder)

        assertTrue(orderMatchResult is OrderMatchEvent.PartialOrderFilled)
        assertNull((orderMatchResult as OrderMatchEvent.PartialOrderFilled).partialSellOrder.orNull())
        assertNotNull((orderMatchResult).partialBuyOrder.orNull())
        assertEquals(500.00, (orderMatchResult).partialBuyOrder.orNull()?.quantity)
    }

    @Test
    fun `do not fill order when buy price is less than sell price`() {
        val buyOrder = Order(side = OrderSide.BUY, quantity = 1000.00, price = 0.8, currencyPair = CurrencyPair.BTCZAR)
        val sellOrder = Order(side = OrderSide.SELL, quantity = 1000.00, price = 1.0, currencyPair = CurrencyPair.BTCZAR)

        val orderMatchResult = matchingEngine.limitMatchOrder(buyOrder, sellOrder)

        assertTrue(orderMatchResult is OrderMatchEvent.OrderNotFilled)
    }

    
    
}