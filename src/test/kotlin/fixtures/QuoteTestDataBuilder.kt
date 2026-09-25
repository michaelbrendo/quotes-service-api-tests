package fixtures

import model.CreateQuoteRequest
import model.CreateQuoteRequestItem

object QuoteTestDataBuilder {
    fun buildCreateQuoteRequest(
        customer: String = "Acme Corp",
        item: String = "Product A",
        quantity: Float = 1.0f,
        unitaryPrice: Double = 100.0,
        discountPercentage: Float = 0.0f,
    ): CreateQuoteRequest {
        return CreateQuoteRequest(
            customer = customer,
            items =
                listOf(
                    CreateQuoteRequestItem(
                        item = item,
                        quantity = quantity,
                        unitaryPrice = unitaryPrice,
                        discountPercentage = discountPercentage,
                    ),
                ),
        )
    }

    fun buildCreateQuoteRequest(
        customer: String = "Acme Corp",
        items: List<CreateQuoteRequestItem>,
    ): CreateQuoteRequest {
        return CreateQuoteRequest(
            customer = customer,
            items = items,
        )
    }
}
