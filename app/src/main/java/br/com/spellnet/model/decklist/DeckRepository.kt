package br.com.spellnet.model.decklist

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.MutableLiveDataResource
import br.com.spellnet.commom.Resource
import br.com.spellnet.database.dao.DeckDao
import br.com.spellnet.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DeckRepository(private val deckService: DeckService, private val deckDao: DeckDao) {

    fun deckList(): LiveDataResource<List<Deck>> = MutableLiveDataResource<List<Deck>>().apply {
        GlobalScope.launch {
            val deckList = GlobalScope.async { deckDao.getAllDecks() }.await()
            postValue(Resource.Success(deckList.map {
                Deck(it.deck.id, it.deck.name, it.deck.importUrl,
                    it.deckSections.map {
                        DeckSection(it.deckSection.title,
                            it.deckCardQuantities.map {
                                CardQuantity(
                                    it.deckCardQuantityEntity.quantity,
                                    Card(it.card.first().name)
                                )
                            })
                    }
                )
            }))
        }
    }

    fun importDeck(deckImport: DeckImport): LiveDataResource<Deck> = MediatorLiveDataResource<Deck>().apply {
        GlobalScope.launch {
            val importedDeck = GlobalScope.async { deckService.importDeck(deckImport) }.await()
            if (importedDeck != null) {
                val deckId = GlobalScope.async(Dispatchers.IO) { deckDao.insertDeck(importedDeck) }.await()
                postValue(Resource.Success(importedDeck.copy(entityId = deckId)))
            } else {
                postValue(Resource.Error())
            }
        }
    }

    suspend fun deleteDeck(deck: Deck) {
        deck.entityId?.let {
            deckDao.deleteDeckById(it)
        }
    }
}