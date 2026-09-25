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

    //Health Check
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

    // AC1: 1 item, no discount (Total: 100.00)
    fun stubCreateQuoteAC1() {
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
                                "customer": "Acme Corp",
                                "revision": 1,
                                "totalPrice": 100.0,
                                "lines": [
                                  {
                                    "item": "Product A",
                                    "quantity": 2.0,
                                    "unitaryPrice": 50.0,
                                    "discountPercentage": 0.0,
                                    "discountAmount": 0.0,
                                    "linePrice": 100.0
                                  }
                                ],
                                "status": "Active"
                              },
                              "confirmation": {
                                "message": "Quote created successfully.",
                                "level": "Success"
                              }
                            }
                            """.trimIndent()
                        )
                )
        )
    }

    // AC2: 1 item with 10% discount (Discount: 10.00, Total: 90.00)
    fun stubCreateQuoteAC2() {
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
                                "id": "b4c9f321-0d23-5e92-98c4-3203249b5231",
                                "customer": "Acme Corp",
                                "revision": 1,
                                "totalPrice": 90.0,
                                "lines": [
                                  {
                                    "item": "Product A",
                                    "quantity": 2.0,
                                    "unitaryPrice": 50.0,
                                    "discountPercentage": 10.0,
                                    "discountAmount": 10.0,
                                    "linePrice": 90.0
                                  }
                                ],
                                "status": "Active"
                              },
                              "confirmation": {
                                "message": "Quote created successfully.",
                                "level": "Success"
                              }
                            }
                            """.trimIndent()
                        )
                )
        )
    }

    // AC3: 2 items (Product A + Product B, Total: 200.00)
    fun stubCreateQuoteAC3() {
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
                                "id": "c5da0432-1e34-6a03-09d5-4314350c6342",
                                "customer": "Acme Corp",
                                "revision": 1,
                                "totalPrice": 200.0,
                                "lines": [
                                  {
                                    "item": "Product A",
                                    "quantity": 2.0,
                                    "unitaryPrice": 50.0,
                                    "discountPercentage": 0.0,
                                    "discountAmount": 0.0,
                                    "linePrice": 100.0
                                  },
                                  {
                                    "item": "Product B",
                                    "quantity": 1.0,
                                    "unitaryPrice": 100.0,
                                    "discountPercentage": 0.0,
                                    "discountAmount": 0.0,
                                    "linePrice": 100.0
                                  }
                                ],
                                "status": "Active"
                              },
                              "confirmation": {
                                "message": "Quote created successfully.",
                                "level": "Success"
                              }
                            }
                            """.trimIndent()
                        )
                )
        )
    }

    // Negative Scenario: Bad Request
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

    // ADDITIONAL ACCEPTANCE CRITERIA - HAPPY PATH
    fun stubCreateQuoteMixedDiscounts() {
        wireMockServer?.stubFor(
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
                                "id": "d6ea0543-2f45-4a14-10e6-5425461d7453",
                                "customer": "Acme Corp",
                                "revision": 1,
                                "totalPrice": 680.0,
                                "lines": [
                                  {
                                    "item": "Product A",
                                    "quantity": 2.0,
                                    "unitaryPrice": 100.0,
                                    "discountPercentage": 10.0,
                                    "discountAmount": 20.0,
                                    "linePrice": 180.0
                                  },
                                  {
                                    "item": "Product B",
                                    "quantity": 1.0,
                                    "unitaryPrice": 500.0,
                                    "discountPercentage": 0.0,
                                    "discountAmount": 0.0,
                                    "linePrice": 500.0
                                  }
                                ],
                                "status": "Active"
                              },
                              "confirmation": {
                                "message": "Quote created successfully.",
                                "level": "Success"
                              }
                            }
                            """.trimIndent()
                        )
                )
        )
    }

    // NEGATIVE CASES Error (HTTP 400 Bad Request)
    fun stubCreateQuoteMissingCustomer() {
        wireMockServer?.stubFor(
            post(urlEqualTo("/api/Quotes/create"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                              "confirmation": {
                                "message": "Customer name is required and cannot be empty.",
                                "level": "Error"
                              }
                            }
                            """.trimIndent()
                        )
                )
        )
    }

    // NEGATIVE CASES Error (HTTP 400 Bad Request)
    fun stubCreateQuoteEmptyItems() {
        wireMockServer?.stubFor(
            post(urlEqualTo("/api/Quotes/create"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                              "confirmation": {
                                "message": "Quote must contain at least one item.",
                                "level": "Error"
                              }
                            }
                            """.trimIndent()
                        )
                )
        )
    }
}