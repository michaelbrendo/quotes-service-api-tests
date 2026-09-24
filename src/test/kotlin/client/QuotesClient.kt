package client

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import model.CreateQuoteRequest

class QuotesClient(private val baseUrl: String) {

    fun getIsAlive(): Response {
        return given()
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .`when`()
            .get("/api/Quotes/isalive")
    }

    fun postCreateQuote(requestPayload: CreateQuoteRequest): Response {
        return given()
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(requestPayload)
            .`when`()
            .post("/api/Quotes/create")
    }
}