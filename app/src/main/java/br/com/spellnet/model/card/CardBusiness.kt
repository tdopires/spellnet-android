package br.com.spellnet.model.card

import br.com.spellnet.commom.*
import br.com.spellnet.model.card.Card
import br.com.spellnet.model.deck.Deck

class CardBusiness(val cardRepository: CardRepository) {

    fun fetchCardPricing(card: Card): LiveDataResource<CardPricing> = MediatorLiveDataResource<CardPricing>().apply {
        postValue(Resource.Loading())
        addResourceSourceWithRetries(fun () = cardRepository.fetchCardPricing(card), 3) {
            postValue(it)
        }
    }

}