package tests

import client.QuotesClient
import mock.QuotesMockServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach

abstract class BaseTest {

    protected lateinit var quotesClient: QuotesClient

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupMockServer() {
            QuotesMockServer.start()
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
        quotesClient = QuotesClient(QuotesMockServer.getBaseUrl())
    }
}