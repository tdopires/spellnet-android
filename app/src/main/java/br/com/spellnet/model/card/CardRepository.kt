package br.com.spellnet.model.card

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CardRepository(val cardService: CardService) {

    fun fetchCardPricing(card: Card): LiveDataResource<CardPricing> = MediatorLiveDataResource<CardPricing>().apply {
        GlobalScope.launch {
            val cardPricing = GlobalScope.async(Dispatchers.IO) { cardService.fetchCardPricing(card) }.await()
            if (cardPricing != null) {
                postValue(Resource.Success(cardPricing))
            } else {
                postValue(Resource.Error())
            }
        }
    }

}