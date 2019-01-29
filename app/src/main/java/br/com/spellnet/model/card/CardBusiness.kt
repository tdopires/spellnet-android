package br.com.spellnet.model.card

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.Resource
import br.com.spellnet.commom.addResourceSource
import br.com.spellnet.model.card.Card
import br.com.spellnet.model.deck.Deck

class CardBusiness(val cardRepository: CardRepository) {

    fun fetchCardPricing(card: Card): LiveDataResource<CardPricing> = MediatorLiveDataResource<CardPricing>().apply {
        postValue(Resource.Loading())
        addResourceSource(cardRepository.fetchCardPricing(card)) {
            postValue(it)
        }
    }

}