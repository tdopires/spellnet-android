package br.com.spellnet.features.deckdetail.viewmodel

import android.arch.lifecycle.ViewModel
import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.entity.Card
import br.com.spellnet.model.deckdetail.CardBusiness
import br.com.spellnet.entity.CardPricing
import br.com.spellnet.entity.Deck
import br.com.spellnet.entity.fullFlatCardsList

class DeckDetailViewModel(val cardBusiness: CardBusiness) : ViewModel() {

    fun openDeck(deck: Deck): Map<Card, LiveDataResource<CardPricing>> {
        return deck.fullFlatCardsList()
            .map { it to cardBusiness.fetchCardPricing(it) }.toMap()
    }

    fun retryFetchCardPricing(card: Card): LiveDataResource<CardPricing> = cardBusiness.fetchCardPricing(card)

}