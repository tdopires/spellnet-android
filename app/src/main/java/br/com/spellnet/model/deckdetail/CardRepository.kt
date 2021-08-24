package br.com.spellnet.model.deckdetail

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.Resource
import br.com.spellnet.database.dao.CardPriceCacheDao
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardPricing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardRepository(
    val cardService: CardService,
    val cardPriceCacheDao: CardPriceCacheDao
) {

    fun fetchCardPricing(card: Card): LiveDataResource<CardPricing> = MediatorLiveDataResource<CardPricing>().apply {
        GlobalScope.launch {
            val cardPricingFromCache = withContext(Dispatchers.IO) { retrieveCardPricingCache(card) }
            if (cardPricingFromCache != null) {
                postValue(Resource.Success(cardPricingFromCache))
            }

            val cardPricing = withContext(Dispatchers.IO) { cardService.fetchCardPricing(card) }
            if (cardPricing != null) {
                updateCardPricingCache(card, cardPricing)
                postValue(Resource.Success(cardPricing))
            } else {
                postValue(Resource.Error())
            }
        }
    }

    suspend fun clearAllCardPriceCache() {
        cardPriceCacheDao.deleteAllCardPriceCache()
    }

    private fun retrieveCardPricingCache(card: Card): CardPricing?  {
        val cardPriceCacheEntity = cardPriceCacheDao.getCardPriceCacheByCardNameWithRefreshPeriod(card.name)
        return cardPriceCacheEntity?.let {
            CardPricing(
                card,
                minPrice = it.minPrice?.toBigDecimal(),
                midPrice = it.midPrice?.toBigDecimal(),
                maxPrice = it.maxPrice?.toBigDecimal()
            )
        }
    }

    private fun updateCardPricingCache(card: Card, cardPricing: CardPricing)  {
        cardPriceCacheDao.upsertCardPriceCache(
            cardName = card.name,
            minPrice = cardPricing.minPrice,
            midPrice = cardPricing.midPrice,
            maxPrice = cardPricing.maxPrice
        )
    }
}