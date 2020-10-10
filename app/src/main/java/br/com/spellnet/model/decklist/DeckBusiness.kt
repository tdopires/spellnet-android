package br.com.spellnet.model.decklist

import br.com.spellnet.commom.*
import br.com.spellnet.entity.Deck
import br.com.spellnet.entity.DeckImport

class DeckBusiness(private val deckRepository: DeckRepository) {

    fun deckList(): LiveDataResource<List<Deck>> = MediatorLiveDataResource<List<Deck>>().apply {
        postValue(Resource.Loading())
        addResourceSource(deckRepository.deckList()) {
            postValue(it)
        }
    }

    fun importDeck(deckImport: DeckImport): LiveDataResource<Deck> = MediatorLiveDataResource<Deck>().apply {
        postValue(Resource.Loading())
        addResourceSource(deckRepository.importDeck(deckImport)) {
            postValue(it)
        }
    }

    suspend fun deleteDeck(deck: Deck) {
        deckRepository.deleteDeck(deck)
    }
}

