package tests

import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
import mock.QuotesMockServer
import model.CreateQuoteRequest
import model.CreateQuoteRequestItem
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CreateQuoteContractTest : BaseTest() {
    @Test
    @DisplayName("Contract: Validate CreateQuoteResponse payload against JSON Schema")
    fun shouldValidateCreateQuoteJsonSchema() {
        QuotesMockServer.stubCreateQuoteAC1()

        val requestPayload =
            CreateQuoteRequest(
                customer = "Acme Corp",
                items =
                    listOf(
                        CreateQuoteRequestItem(
                            item = "Product A",
                            quantity = 2.0f,
                            unitaryPrice = 50.0,
                            discountPercentage = 0.0f,
                        ),
                    ),
            )

        val response = quotesClient.postCreateQuote(requestPayload)

        response.then().log().all()

        response.then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("schemas/create-quote-response-schema.json"))
    }
}
