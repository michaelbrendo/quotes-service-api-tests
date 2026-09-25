package client

import io.restassured.RestAssured.given
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import model.CreateQuoteRequest

class QuotesClient(
    private val baseUrl: String,
    private val requestSpec: RequestSpecification
) {

    fun getIsAlive(): Response {
        return given()
            .spec(requestSpec)
            .baseUri(baseUrl)
            .`when`()
            .get("/api/Quotes/isalive")
    }

    fun postCreateQuote(requestPayload: CreateQuoteRequest): Response {
        return given()
            .spec(requestSpec)
            .baseUri(baseUrl)
            .body(requestPayload)
            .`when`()
            .post("/api/Quotes/create")
    }
}