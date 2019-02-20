package br.com.spellnet.model.cardcollection

import br.com.spellnet.database.dao.CardCollectionDao
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardQuantity

class CardCollectionRepository(private val cardCollectionDao: CardCollectionDao) {

    fun fetchHaveCardQuantity(card: Card): CardQuantity? {
        return cardCollectionDao.getCardQuantityByCardName(card.name)?.let {
            CardQuantity(it.quantity, card)
        }
    }

    fun updateCardCollection(haveCardQuantity: CardQuantity) {
        cardCollectionDao.upsertCardQuantities(haveCardQuantity.card.name, haveCardQuantity.quantity)
    }

}