package br.com.spellnet.servermodel

import br.com.spellnet.commom.safeLet
import br.com.spellnet.model.card.Card
import br.com.spellnet.model.card.CardPricing

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

    fun List<String>.toCardPrices(): CardPricing? = if (this.size == 3) {
        val sortedPrices =
            this.map { it.toBigDecimalOrNull() }.sortedBy { it }.toList()
        safeLet(sortedPrices[0], sortedPrices[1], sortedPrices[2]) { minPrice, midPrice, maxPrice ->
            if (minPrice.toDouble() != 0.0 && midPrice.toDouble() != 0.0 && maxPrice.toDouble() != 0.0) {
                CardPricing(card, minPrice, midPrice, maxPrice)
            } else null
        } ?: run { null }
    } else null

    val cardPrices: List<List<String>> = data.prices.values.map { it.BRL }.toList()
    return cardPrices.map { it.toCardPrices() }.filterNotNull().sortedBy { it.minPrice }.firstOrNull()
}