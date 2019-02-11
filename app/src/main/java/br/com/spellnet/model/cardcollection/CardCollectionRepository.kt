package br.com.spellnet.model.cardcollection

import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardCollection
import br.com.spellnet.entity.CardQuantity

class CardCollectionRepository {

    private var mockedCardCollection = CardCollection(emptyList()) //TODO change it to room database

    fun fetchHaveCardQuantity(card: Card) =
            mockedCardCollection.haveList.firstOrNull { it.card == card }

    fun updateCardCollection(haveCardQuantity: CardQuantity) {
        val cardIndex = mockedCardCollection.haveList.indexOfFirst { it.card == haveCardQuantity.card }
        val updatedList = mockedCardCollection.haveList.toMutableList()

        if (cardIndex >= 0) {
            updatedList[cardIndex] = haveCardQuantity
        } else {
            updatedList.add(haveCardQuantity)
        }
        mockedCardCollection = mockedCardCollection.copy(haveList = updatedList)
    }

}