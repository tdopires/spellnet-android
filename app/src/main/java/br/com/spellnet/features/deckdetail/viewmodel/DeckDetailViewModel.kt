package br.com.spellnet.features.deckdetail.viewmodel

import androidx.lifecycle.ViewModel
import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.entity.*
import br.com.spellnet.model.cardcollection.CardCollectionBusiness
import br.com.spellnet.model.deckdetail.CardBusiness

class DeckDetailViewModel(private val cardBusiness: CardBusiness, private val cardCollectionBusiness: CardCollectionBusiness) :
    ViewModel() {

    fun openDeck(deck: Deck): Map<Card, Pair<LiveDataResource<CardQuantity>, LiveDataResource<CardPricing>>> {
        return deck.fullFlatCardsList()
            .map { it to Pair(cardCollectionBusiness.fetchHaveCardQuantity(it), cardBusiness.fetchCardPricing(it)) }
            .toMap()
    }

    fun retryFetchCardPricing(card: Card): LiveDataResource<CardPricing> = cardBusiness.fetchCardPricing(card)

    fun updateCardCollection(haveCardQuantity: CardQuantity) =
        cardCollectionBusiness.updateCardCollection(haveCardQuantity)

}