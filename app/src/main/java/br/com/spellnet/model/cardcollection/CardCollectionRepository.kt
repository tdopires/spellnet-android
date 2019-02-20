package br.com.spellnet.model.cardcollection

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.Resource
import br.com.spellnet.database.dao.CardCollectionDao
import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardQuantity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CardCollectionRepository(private val cardCollectionDao: CardCollectionDao) {

    fun fetchHaveCardQuantity(card: Card): LiveDataResource<CardQuantity> {
        return MediatorLiveDataResource<CardQuantity>().apply {
            GlobalScope.launch {
                val cardCollectionQuantityEntity =
                    GlobalScope.async(Dispatchers.IO) { cardCollectionDao.getCardQuantityByCardName(card.name) }.await()
                if (cardCollectionQuantityEntity != null) {
                    postValue(Resource.Success(CardQuantity(cardCollectionQuantityEntity.quantity, card)))
                } else {
                    postValue(Resource.Error())
                }
            }
        }
    }

    fun updateCardCollection(haveCardQuantity: CardQuantity) {
        GlobalScope.launch {
            GlobalScope.async(Dispatchers.IO) {
                cardCollectionDao.upsertCardQuantities(haveCardQuantity.card.name, haveCardQuantity.quantity)
            }
        }
    }

}