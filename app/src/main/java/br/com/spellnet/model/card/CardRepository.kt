package br.com.spellnet.model.card

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class CardRepository(val cardService: CardService) {

    fun fetchCardPricing(card: Card): LiveDataResource<CardPricing> = MediatorLiveDataResource<CardPricing>().apply {
        runBlocking {
            val cardPricing = async { cardService.fetchCardPricing(card) }.await()
            if (cardPricing != null) {
                postValue(Resource.Success(cardPricing))
            } else {
                postValue(Resource.Error())
            }
        }
    }

}