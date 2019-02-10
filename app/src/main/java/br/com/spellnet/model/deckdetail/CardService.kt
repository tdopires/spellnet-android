package br.com.spellnet.model.deckdetail

import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardPricing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException
import java.math.BigDecimal
import java.util.*

private val FREE_CARD_LIST = listOf("mountain", "island", "plains", "forest", "swamp")

class CardService {

    private var cardPricingService: CardPricingService

    init {
        cardPricingService = CardPricingServer.retrofit.create(CardPricingService::class.java)
    }

    suspend fun fetchCardPricing(card: Card): CardPricing? = withContext(Dispatchers.IO) {
        if (FREE_CARD_LIST.contains(card.name.toLowerCase(Locale.US))) {
            CardPricing(card, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
        } else {
            try {
                val response = cardPricingService.getCardPricing(card.name).execute()
                if (response.isSuccessful) {
                    response.body()?.toCardPricing(card)
                } else null
            } catch (e: IOException) {
                null
            }
        }
    }

    interface CardPricingService {

        @GET("/ligamagic/card/{cardName}")
        fun getCardPricing(@Path("cardName") cardName: String): Call<CardPricingResponse>
    }

}