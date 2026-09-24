package model

data class CreateQuoteRequest(
    val customer: String,
    val items: List<CreateQuoteRequestItem>)

data class CreateQuoteRequestItem(
    val item: String,
    val quantity: Float,
    val unitaryPrice: Double,
    val discountPercentage: Float = 0.0f
)
