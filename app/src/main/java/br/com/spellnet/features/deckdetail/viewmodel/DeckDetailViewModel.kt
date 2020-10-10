package br.com.spellnet.features.deckdetail.viewmodel

import android.graphics.Path
import androidx.lifecycle.ViewModel
import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.Resource
import br.com.spellnet.entity.*
import br.com.spellnet.model.cardcollection.CardCollectionBusiness
import br.com.spellnet.model.deckdetail.CardBusiness

typealias OpenedDeck = Map<Card, Pair<LiveDataResource<CardQuantity>, LiveDataResource<CardPricing>>>

class DeckDetailViewModel(private val cardBusiness: CardBusiness, private val cardCollectionBusiness: CardCollectionBusiness) :
    ViewModel() {

    private val openedDeck = MediatorLiveDataResource<OpenedDeck>().apply {
        postValue(Resource.Loading())
    }

    fun deck(): LiveDataResource<OpenedDeck> = openedDeck

    fun openDeck(deck: Deck) {
        openedDeck.postValue(
            Resource.Success(
                deck.fullFlatCardsList()
                    .map {
                        it to Pair(
                            cardCollectionBusiness.fetchHaveCardQuantity(it),
                            cardBusiness.fetchCardPricing(it)
                        )
                    }
                    .toMap()
            )
        )
    }

    fun retryFetchCardPricing(card: Card): LiveDataResource<CardPricing> = cardBusiness.fetchCardPricing(card)

    fun updateCardCollection(haveCardQuantity: CardQuantity) =
        cardCollectionBusiness.updateCardCollection(haveCardQuantity)

}