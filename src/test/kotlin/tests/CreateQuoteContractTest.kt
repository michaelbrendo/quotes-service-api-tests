package tests

import fixtures.QuoteTestDataBuilder
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel
import io.qameta.allure.Story
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
import mock.QuotesMockServer
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Epic("Quotes Management")
@Feature("Contract Validation")
class CreateQuoteContractTest : BaseTest() {
    @Test
    @Tag("Contract")
    @Story("Contract - Validate CreateQuoteResponse JSON Schema")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Validate CreateQuote Response Payload Schema Contract")
    fun shouldValidateCreateQuoteJsonSchema() {
        QuotesMockServer.stubCreateQuoteAC1()

        val requestPayload =
            QuoteTestDataBuilder.buildCreateQuoteRequest(
                customer = "Acme Corp",
            )

        val response = quotesClient.postCreateQuote(requestPayload)

        response.then()
            .statusCode(200)
            .assertThat()
            .body(matchesJsonSchemaInClasspath("schemas/create-quote-response-schema.json"))
    }
}
