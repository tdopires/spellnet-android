package br.com.spellnet.model.deckdetail

import android.util.Log
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardPricing
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException
import java.math.BigDecimal
import java.net.URLEncoder
import java.util.*

private val FREE_CARD_LIST = listOf("mountain", "island", "plains", "forest", "swamp")

class CardService {

    private var cardPricingService: CardPricingService

    private val gson: Gson = GsonBuilder()
        .enableComplexMapKeySerialization()
        .create()
    private var functions: FirebaseFunctions

    init {
        cardPricingService = CardPricingServer.retrofit.create(CardPricingService::class.java)
        functions = Firebase.functions
    }

    suspend fun fetchCardPricing(card: Card): CardPricing? = withContext(Dispatchers.IO) {
        if (FREE_CARD_LIST.contains(card.name.toLowerCase(Locale.US))) {
            CardPricing(card, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
        } else {
            try {
                val response = cardPricingService.getCardPricing(card.name).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.status == "ok") {
                            it.toCardPricing(card)
                        } else null
                    }
                } else null
            } catch (e: IOException) {
                null
            }
        }
    }

    suspend fun fetchCardPricingFirebase(card: Card): CardPricing? = withContext(Dispatchers.IO) {
        val data = hashMapOf(
            "cardName" to URLEncoder.encode(card.name, "UTF-8")
        )

        try {
            Log.d("AAAA", "findCardPrices")

            val httpsCallableResult = functions
                .getHttpsCallable("findCardPrices")
                .call(data)
                .await()

            Log.d("AAAA", "await finished")

            val jsonString = gson.toJson(httpsCallableResult.data)
            Log.d("AAAA", "jsonString")

            val cardPricingResponse = gson.fromJson(jsonString, CardPricingResponse::class.java)
            Log.d("AAAA", "cardPricingResponse")

            if (cardPricingResponse.status == "ok") {
                cardPricingResponse.toCardPricing(card)
            } else null
        } catch (e: FirebaseFunctionsException) {
            Log.d("AAAA", "error", e)
            null
        }
    }

    interface CardPricingService {

        @GET("/ligamagic/card/{cardName}")
        fun getCardPricing(@Path("cardName") cardName: String): Call<CardPricingResponse>
    }

}