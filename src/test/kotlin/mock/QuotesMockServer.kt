package mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

object QuotesMockServer {

    private const val PORT = 8089
    private var wireMockServer: WireMockServer? = null

    fun start() {
        if (wireMockServer == null) {
            wireMockServer = WireMockServer(wireMockConfig().port(PORT))
            wireMockServer?.start()
            configureFor("localhost", PORT)
        }
    }

    fun stop() {
        wireMockServer?.stop()
        wireMockServer = null
    }

    fun reset() {
        wireMockServer?.resetAll()
    }

    fun getBaseUrl(): String = "http://localhost:$PORT"

    // Stub: Health Check (GET /api/Quotes/isalive)
    fun stubIsAliveSuccess() {
        stubFor(
            get(urlEqualTo("/api/Quotes/isalive"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"status": "healthy"}""")
                )
        )
    }

    // Stub: Post Create Quote (POST /api/Quotes/create - 200 OK)
    fun stubCreateQuoteSuccess() {
        stubFor(
            post(urlEqualTo("/api/Quotes/create"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                              "quote": {
                                "id": "a3b8e210-9c12-4f81-87b3-2192138a4120",
                                "customer": "ACME Corp",
                                "revision": 1,
                                "totalPrice": 1800.0,
                                "lines": [
                                  {
                                    "item": "Software License",
                                    "quantity": 2.0,
                                    "unitaryPrice": 1000.0,
                                    "discountPercentage": 10.0,
                                    "discountAmount": 200.0,
                                    "linePrice": 1800.0
                                  }
                                ],
                                "status": "Active"
                              },
                              "confirmation": {
                                "message": "Quote created successfully",
                                "level": "Success"
                              }
                            }
                            """.trimIndent()
                        )
                )
        )
    }

    // Stub: Bad Request (400 Bad Request)
    fun stubCreateQuoteBadRequest() {
        stubFor(
            post(urlEqualTo("/api/Quotes/create"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                              "error": "Bad Request",
                              "message": "Customer name is mandatory"
                            }
                            """.trimIndent()
                        )
                )
        )
    }
}