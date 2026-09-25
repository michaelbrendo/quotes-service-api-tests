package tests

import fixtures.QuoteTestDataBuilder
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel
import io.qameta.allure.Story
import mock.QuotesMockServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("NegativeCases")
class CreateQuoteNegativeTest : BaseTest() {
    @Test
    @Story("Negative - Attempt to create a quote without customer name")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Negative: Attempt to create a quote without customer name")
    fun shouldReturnErrorWhenCustomerIsMissing() {
        QuotesMockServer.stubCreateQuoteMissingCustomer()

        val requestPayload =
            QuoteTestDataBuilder.buildCreateQuoteRequest(
                customer = "",
            )

        val response = quotesClient.postCreateQuote(requestPayload)
        assertEquals(400, response.statusCode)

        val levelName = response.jsonPath().getString("confirmation.level")
        val message = response.jsonPath().getString("confirmation.message")

        assertEquals("Error", levelName)
        assertTrue(
            message?.contains("customer", ignoreCase = true) == true,
            "Expected error message to mention 'customer', but got: $message",
        )
    }

    @Test
    @Story("Negative - Attempt to create a quote without any items")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Negative: Attempt to create a quote without items")
    fun shouldReturnErrorWhenItemsListIsEmpty() {
        QuotesMockServer.stubCreateQuoteEmptyItems()

        val requestPayload =
            QuoteTestDataBuilder.buildCreateQuoteRequest(
                items = emptyList(),
            )

        val response = quotesClient.postCreateQuote(requestPayload)
        assertEquals(400, response.statusCode)

        val levelName = response.jsonPath().getString("confirmation.level")
        val message = response.jsonPath().getString("confirmation.message")

        assertEquals("Error", levelName)
        assertTrue(
            message?.contains("item", ignoreCase = true) == true,
            "Expected error message to mention 'item', but got: $message",
        )
    }
}
