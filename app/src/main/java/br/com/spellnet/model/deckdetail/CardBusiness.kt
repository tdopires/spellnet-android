package br.com.spellnet.model.deckdetail

import br.com.spellnet.commom.*
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardPricing

class CardBusiness(val cardRepository: CardRepository) {

    fun fetchCardPricing(card: Card): LiveDataResource<CardPricing> = MediatorLiveDataResource<CardPricing>().apply {
        postValue(Resource.Loading())
        addResourceSourceWithRetries(fun () = cardRepository.fetchCardPricing(card), 3) {
            postValue(it)
        }
    }

}