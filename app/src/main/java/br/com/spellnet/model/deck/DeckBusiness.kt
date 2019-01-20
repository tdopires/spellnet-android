package br.com.spellnet.model.deck

import br.com.spellnet.commom.*

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

}

