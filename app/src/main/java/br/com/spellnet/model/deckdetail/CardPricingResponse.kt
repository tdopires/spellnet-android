package br.com.spellnet.model.deckdetail

import br.com.spellnet.commom.safeLet
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardPricing
import java.math.BigDecimal
import java.util.*

data class CardPricingResponse(val data: CardPricingResponseData, val status: String)

data class CardPricingResponseData(
    val card: String,
    val currencies: List<String>,
    val prices: Map<String, CardPricingByCurrencyResponse>,
    val sets: List<String>,
    val url: String
)

data class CardPricingByCurrencyResponse(val BRL: List<String>)

fun CardPricingResponse.toCardPricing(card: Card): CardPricing? {

    fun normalizeAndParseDoubleString(str: String): BigDecimal? {
        return if (str.count { it == '.' } > 1) {
            var newStr = str
            while (newStr.count { it == '.' } > 1) {
                newStr = newStr.replaceFirst(".", "")
            }
            newStr.toBigDecimalOrNull()
        } else {
            str.toBigDecimalOrNull()
        }
    }

    fun List<String>.toCardPrices(): CardPricing? = if (this.size == 3) {
        val sortedPrices =
            this.map { normalizeAndParseDoubleString(it) }.sortedBy { it }.toList()
        safeLet(sortedPrices[0], sortedPrices[1], sortedPrices[2]) { minPrice, midPrice, maxPrice ->
            if (minPrice.toDouble() != 0.0 && midPrice.toDouble() != 0.0 && maxPrice.toDouble() != 0.0) {
                CardPricing(card, minPrice, midPrice, maxPrice)
            } else null
        } ?: run { null }
    } else null

    val invalidEditions = listOf("wcd")
    val isValidEdition: (String) -> Boolean = { edition ->
        invalidEditions.contains(edition.toLowerCase(Locale.US)).not()
    }

    val cardPrices: List<List<String>> = data.prices.filterKeys(isValidEdition).values.map { it.BRL }.toList()
    return cardPrices.map { it.toCardPrices() }.filterNotNull().sortedBy { it.minPrice }.firstOrNull()
}