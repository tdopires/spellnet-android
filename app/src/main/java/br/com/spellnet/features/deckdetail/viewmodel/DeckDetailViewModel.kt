package br.com.spellnet.features.deckdetail.viewmodel

import android.arch.lifecycle.ViewModel
import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.entity.*
import br.com.spellnet.model.cardcollection.CardCollectionBusiness
import br.com.spellnet.model.deckdetail.CardBusiness

class DeckDetailViewModel(val cardBusiness: CardBusiness, val cardCollectionBusiness: CardCollectionBusiness) :
    ViewModel() {

    fun openDeck(deck: Deck): Map<Card, Pair<CardQuantity?, LiveDataResource<CardPricing>>> {
        return deck.fullFlatCardsList()
            .map { it to Pair(cardCollectionBusiness.fetchHaveCardQuantity(it), cardBusiness.fetchCardPricing(it)) }
            .toMap()
    }

    fun retryFetchCardPricing(card: Card): LiveDataResource<CardPricing> = cardBusiness.fetchCardPricing(card)

    fun updateCardCollection(haveCardQuantity: CardQuantity) =
        cardCollectionBusiness.updateCardCollection(haveCardQuantity)

}