package tests

import mock.QuotesMockServer
import model.CreateQuoteRequest
import model.CreateQuoteRequestItem
import model.CreateQuoteResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CreateQuoteNegativeTest : BaseTest() {
    @Test
    @DisplayName("Negative: Attempt to create a quote without customer name")
    fun shouldReturnErrorWhenCustomerIsMissing() {
        QuotesMockServer.stubCreateQuoteMissingCustomer()

        val requestPayload =
            CreateQuoteRequest(
                customer = "",
                items =
                    listOf(
                        CreateQuoteRequestItem(
                            item = "Product A",
                            quantity = 1.0f,
                            unitaryPrice = 100.0,
                            discountPercentage = 0.0f,
                        ),
                    ),
            )

        val response =
            quotesClient.postCreateQuote(requestPayload)
                .then()
                .statusCode(400)
                .extract()
                .`as`(CreateQuoteResponse::class.java)

        assertThat(response.confirmation.level.name).isEqualTo("Error")
        assertThat(response.confirmation.message).containsIgnoringCase("customer")
    }

    @Test
    @DisplayName("Negative: Attempt to create a quote without items")
    fun shouldReturnErrorWhenItemsListIsEmpty() {
        // Arrange
        QuotesMockServer.stubCreateQuoteEmptyItems()

        val requestPayload =
            CreateQuoteRequest(
                customer = "Acme Corp",
                items = emptyList(),
            )

        val response =
            quotesClient.postCreateQuote(requestPayload)
                .then()
                .statusCode(400)
                .extract()
                .`as`(CreateQuoteResponse::class.java)

        assertThat(response.confirmation.level.name).isEqualTo("Error")
        assertThat(response.confirmation.message).containsIgnoringCase("item")
    }
}
