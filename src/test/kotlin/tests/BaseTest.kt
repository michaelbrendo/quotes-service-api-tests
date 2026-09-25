package tests

import client.QuotesClient
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import mock.QuotesMockServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach

abstract class BaseTest {
    protected lateinit var quotesClient: QuotesClient

    companion object {
        private lateinit var requestSpec: RequestSpecification

        @JvmStatic
        @BeforeAll
        fun setupMockServer() {
            QuotesMockServer.start()

            requestSpec =
                RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .addFilter(AllureRestAssured())
                    .build()
        }

        @JvmStatic
        @AfterAll
        fun teardownMockServer() {
            QuotesMockServer.stop()
        }
    }

    @BeforeEach
    fun setupClient() {
        QuotesMockServer.reset()

//        RestAssured.baseURI = QuotesMockServer.getBaseUrl()
        quotesClient = QuotesClient(QuotesMockServer.getBaseUrl(), requestSpec)
    }
}
