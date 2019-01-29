package br.com.spellnet.decklist.viewmodel

import android.arch.lifecycle.ViewModel
import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.model.card.CardBusiness
import br.com.spellnet.model.card.CardPricing
import br.com.spellnet.model.deck.Deck
import br.com.spellnet.model.deck.fullFlatCardsList

class DeckDetailViewModel(val cardBusiness: CardBusiness) : ViewModel() {

    fun openDeck(deck: Deck): List<LiveDataResource<CardPricing>> {
        return deck.fullFlatCardsList()
            .map { cardBusiness.fetchCardPricing(it) }
    }

}