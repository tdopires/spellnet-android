package br.com.spellnet.model.cardcollection

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardQuantity


class CardCollectionBusiness(private val cardCollectionRepository: CardCollectionRepository) {

    fun fetchHaveCardQuantity(card: Card): LiveDataResource<CardQuantity> =
        cardCollectionRepository.fetchHaveCardQuantity(card)

    fun updateCardCollection(haveCardQuantity: CardQuantity) =
        cardCollectionRepository.updateCardCollection(haveCardQuantity)

}