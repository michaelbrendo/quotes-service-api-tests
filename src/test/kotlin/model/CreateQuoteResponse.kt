package model

import org.mozilla.javascript.tools.debugger.Dim
import java.util.UUID
import javax.sound.sampled.Line

data class CreateQuoteResponse(
    val quote: Quote,
    val confirmation: Confirmation
)

data class Quote(
    val id: UUID,
    val customer: String,
    val revision: Int = 1,
    val totalPrice: Double,
    val lines: List<QuoteLine>,
    val status: QuoteStatus
)

data class QuoteLine(
    val item: String,
    val quantity: Float,
    val unitaryPrice: Double,
    val discountPercentage: Float = 0.0f,
    val discountAmount: Double = 0.0,
    val linePrice: Double,
)

data class Confirmation(
    val message: String,
    val level: ConfirmationLevel
)