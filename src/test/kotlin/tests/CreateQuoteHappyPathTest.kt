package tests

import fixtures.QuoteTestDataBuilder
import io.qameta.allure.Allure
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel
import io.qameta.allure.Story
import io.restassured.response.Response
import mock.QuotesMockServer
import model.CreateQuoteRequest
import model.CreateQuoteRequestItem
import model.CreateQuoteResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

@Epic("Quotes Management API")
@Feature("Create Quote Scenarios")
class CreateQuoteHappyPathTest : BaseTest() {
    @Test
    @Tag("HealthCheck")
    @Tag("Sanity")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("HealthCheck: Verify service availability and endpoint status 200")
    fun shouldVerifyHealthCheck() {
        QuotesMockServer.stubIsAliveSuccess()

        val response = quotesClient.getIsAlive()
        assertEquals(200, response.statusCode)
    }

    @Test
    @Tag("AC1")
    @Story("AC1 - Successfully create a new quote with one item without discount")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("AC1: Successfully create a new quote with one item for a customer")
    fun ac1_shouldCreateQuoteWithSingleItemWithoutDiscount() {
        lateinit var requestPayload: CreateQuoteRequest
        lateinit var response: Response

        Allure.step(
            "Given a customer 'Acme Corp' and one item with quantity 2.0 and price 50.0 without discount",
            Allure.ThrowableContextRunnableVoid {
                QuotesMockServer.stubCreateQuoteAC1()
                requestPayload =
                    QuoteTestDataBuilder.buildCreateQuoteRequest(
                        customer = "AcmeCorp",
                        quantity = 2.0f,
                        unitaryPrice = 50.0,
                        discountPercentage = 0.0f,
                    )
            },
        )

        Allure.step(
            "When I request to create a quote for this customer",
            Allure.ThrowableContextRunnableVoid {
                response = quotesClient.postCreateQuote(requestPayload)
            },
        )

        Allure.step(
            "Then the quote is successfully created with total price calculated",
            Allure.ThrowableContextRunnableVoid {
                assertEquals(200, response.statusCode)

                val responseBody = response.`as`(CreateQuoteResponse::class.java)
                assertNotNull(responseBody.quote!!.id)
                assertEquals("Acme Corp", responseBody.quote.customer)
                assertEquals(1, responseBody.quote.lines.size)
                assertEquals(100.0, responseBody.quote.lines[0].linePrice)
                assertEquals(100.0, responseBody.quote.totalPrice)
                assertEquals("Quote created successfully.", responseBody.confirmation.message)
            },
        )
    }

    @Test
    @Tag("AC2")
    @Story("AC2 - Single item with discount")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("AC2: Successfully create quote with single item with discount")
    fun ac2_shouldCreateQuoteWithSingleItemWithDiscount() {
        QuotesMockServer.stubCreateQuoteAC2()

        val requestPayload =
            QuoteTestDataBuilder.buildCreateQuoteRequest(
                customer = "Acme Corp",
                item = "Product A",
                quantity = 2.0f,
                unitaryPrice = 50.0,
                discountPercentage = 10.0f,
            )

        val response = quotesClient.postCreateQuote(requestPayload)
        assertEquals(200, response.statusCode)

        val responseBody = response.`as`(CreateQuoteResponse::class.java)
        assertNotNull(responseBody.quote?.id)
        assertEquals("Acme Corp", responseBody.quote.customer)
        assertEquals(10.0, responseBody.quote.lines[0].discountAmount)
        assertEquals(90.0, responseBody.quote.lines[0].linePrice)
        assertEquals(90.0, responseBody.quote.totalPrice)
        assertEquals("Quote created successfully.", responseBody.confirmation.message)
    }

    @Test
    @Tag("AC3")
    @Story("AC3 - Two items for a customer")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("AC3: Successfully create a new quote with two items for a customer")
    fun ac3_shouldCreateQuoteWithTwoItems() {
        QuotesMockServer.stubCreateQuoteAC3()
        val requestPayload =
            QuoteTestDataBuilder.buildCreateQuoteRequest(
                customer = "Acme Corp",
                items =
                    listOf(
                        CreateQuoteRequestItem(
                            item = "Product A",
                            quantity = 2.0f,
                            unitaryPrice = 50.0,
                            discountPercentage = 0.0f,
                        ),
                        CreateQuoteRequestItem(
                            item = "Product B",
                            quantity = 1.0f,
                            unitaryPrice = 100.0,
                            discountPercentage = 0.0f,
                        ),
                    ),
            )

        val response = quotesClient.postCreateQuote(requestPayload)
        assertEquals(200, response.statusCode)

        val responseBody = response.`as`(CreateQuoteResponse::class.java)
        assertNotNull(responseBody.quote?.id)
        assertEquals("Acme Corp", responseBody.quote.customer)
        assertEquals(2, responseBody.quote.lines.size)

        assertEquals(100.0, responseBody.quote.lines[0].linePrice) // 2 * 50.0
        assertEquals(100.0, responseBody.quote.lines[1].linePrice) // 1 * 100.0

        assertEquals(200.0, responseBody.quote.totalPrice)
        assertEquals("Quote created successfully.", responseBody.confirmation.message)
    }

    @Test
    @Tag("HappyPath")
    @Story("Happy Path - Multiple items with mixed discounts")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Happy Path: Create quote with multiple items and mixed discounts")
    fun shouldCreateQuoteWithMultipleItemsAndMixedDiscounts() {
        QuotesMockServer.stubCreateQuoteMixedDiscounts()

        val requestPayload =
            QuoteTestDataBuilder.buildCreateQuoteRequest(
                customer = "Acme Corp",
                items =
                    listOf(
                        CreateQuoteRequestItem(
                            item = "Product A",
                            quantity = 2.0f,
                            unitaryPrice = 100.0,
                            discountPercentage = 10.0f,
                        ),
                        CreateQuoteRequestItem(
                            item = "Product B",
                            quantity = 1.0f,
                            unitaryPrice = 500.0,
                            discountPercentage = 0.0f,
                        ),
                    ),
            )

        val response = quotesClient.postCreateQuote(requestPayload)
        assertEquals(200, response.statusCode)

        val responseBody = response.`as`(CreateQuoteResponse::class.java)
        assertEquals("Success", responseBody.confirmation.level.name)
        assertEquals("Quote created successfully.", responseBody.confirmation.message)

        assertNotNull(responseBody.quote?.id)
        assertEquals("Active", responseBody.quote.status.name)
        assertEquals(1, responseBody.quote.revision)
        assertEquals(2, responseBody.quote.lines.size)

        // Product A (100.0 * 2) - 10% = 180.0
        assertEquals(180.0, responseBody.quote.lines.getOrNull(0)?.linePrice)

        // Product B (500.0 * 1) - 0% = 500.0
        assertEquals(500.0, responseBody.quote.lines.getOrNull(1)?.linePrice)

        // Total: 180.0 + 500.0 = 680.0
        assertEquals(680.0, responseBody.quote.totalPrice)
    }
}
