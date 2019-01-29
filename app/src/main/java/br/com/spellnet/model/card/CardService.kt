package br.com.spellnet.model.card

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException

class CardService {

    private var cardPricingService: CardPricingService

    init {
        cardPricingService = CardPricingServer.retrofit.create(CardPricingService::class.java)
    }

    suspend fun fetchCardPricing(card: Card): CardPricing? = withContext(Dispatchers.IO) {
        val response = cardPricingService.getCardPricing(card.name).execute()
        if (response.isSuccessful) {
            try {
                response.body()?.toCardPricing(card)
            } catch (e: IOException) {
                null
            }
        } else null
    }

    interface CardPricingService {

        @GET("/ligamagic/card/{cardName}")
        fun getCardPricing(@Path("cardName") cardName: String): Call<CardPricingResponse>
    }

}