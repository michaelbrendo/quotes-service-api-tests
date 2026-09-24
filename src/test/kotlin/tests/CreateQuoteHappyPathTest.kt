package tests

import mock.QuotesMockServer
import model.CreateQuoteRequest
import model.CreateQuoteRequestItem
import model.CreateQuoteResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class CreateQuoteHappyPathTest : BaseTest() {

    @Test
    @DisplayName("HealthCheck: Success verify endpoint")
    fun shouldVerifyHealthCheck() {
        QuotesMockServer.stubIsAliveSuccess()

        val response = quotesClient.getIsAlive()

        assertEquals(200, response.statusCode)
    }

    @Test
    @DisplayName("Successfully create a new quote with one item for a customer")
    fun ac1_shouldCreateQuoteWithSingleItemWithoutDiscount() {

        QuotesMockServer.stubCreateQuoteAC1()

        val requestPayload = CreateQuoteRequest(
            customer = "Acme Corp",
            items = listOf(
                CreateQuoteRequestItem(
                    item = "Product A",
                    quantity = 2.0f,
                    unitaryPrice = 50.0,
                    discountPercentage = 0.0f
                )
            )
        )

        val response = quotesClient.postCreateQuote(requestPayload)

        assertEquals(200, response.statusCode)

        val responseBody = response. `as` (CreateQuoteResponse::class.java)
        assertNotNull(responseBody.quote.id)
        assertEquals("Acme Corp", responseBody.quote.customer)
        assertEquals(1, responseBody.quote.lines.size)
        assertEquals(100.0, responseBody.quote.lines[0].linePrice)
        assertEquals(100.0, responseBody.quote.totalPrice)
        assertEquals("Quote created successfully.", responseBody.confirmation.message)
    }

    @Test
    @DisplayName("Successfully create a new quote with one item with discount for a customer")
    fun ac2_shouldCreateQuoteWithSingleItemWithDiscount() {

        QuotesMockServer.stubCreateQuoteAC2()

        val requestPayload = CreateQuoteRequest(
            customer = "Acme Corp",
            items = listOf(
                CreateQuoteRequestItem(
                    item = "Product A",
                    quantity = 2.0f,
                    unitaryPrice = 50.0,
                    discountPercentage = 10.0f
                )
            )
        )

        val response = quotesClient.postCreateQuote(requestPayload)

        assertEquals(200, response.statusCode)

        val responseBody = response. `as` (CreateQuoteResponse::class.java)
        assertNotNull(responseBody.quote.id)
        assertEquals("Acme Corp", responseBody.quote.customer)
        assertEquals(10.0, responseBody.quote.lines[0].discountAmount)
        assertEquals(90.0, responseBody.quote.lines[0].linePrice)
        assertEquals(90.0, responseBody.quote.totalPrice)
        assertEquals("Quote created successfully.", responseBody.confirmation.message)
    }

    @Test
    @DisplayName("AC3: Successfully create a new quote with two items for a customer")
    fun ac3_shouldCreateQuoteWithMultipleItems(){

        QuotesMockServer.stubCreateQuoteAC3()

        val requestPayload = CreateQuoteRequest(
            customer = "Acme Corp",
            items = listOf(
                CreateQuoteRequestItem(
                    item = "Product A",
                    quantity = 2.0f,
                    unitaryPrice = 50.0,
                    discountPercentage = 0.0f
                ),
                CreateQuoteRequestItem(
                    item = "Product B",
                    quantity = 1.0f,
                    unitaryPrice = 100.0,
                    discountPercentage = 0.0f
                )

            )
        )

        val response = quotesClient.postCreateQuote(requestPayload)

        assertEquals(200, response.statusCode)

        val responseBody = response. `as` (CreateQuoteResponse::class.java)
        assertNotNull(responseBody.quote.id)
        assertEquals("Acme Corp", responseBody.quote.customer)
        assertEquals(2, responseBody.quote.lines.size)
        assertEquals(200.0, responseBody.quote.totalPrice)
        assertEquals("Quote created successfully.", responseBody.confirmation.message)


    }

}