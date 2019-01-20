package br.com.spellnet.model.deck

import android.arch.lifecycle.MediatorLiveData
import br.com.spellnet.commom.*

class DeckBusiness(private val deckRepository: DeckRepository) {

    fun deckList(): LiveDataResource<List<Deck>> = MediatorLiveDataResource<List<Deck>>().apply {
        postValue(Resource.Loading())
        addResourceSource(deckRepository.deckList()) {
            postValue(it)
        }
    }

    fun importDeck(deckImport: DeckImport): LiveDataResource<Deck> {
        val result = MutableLiveDataResource<Deck>()
        result.postValue(Resource.Loading())
        val deck = Deck(deckImport.name, listOf())
        deckRepository.addDeck(deck)
        result.postValue(Resource.Success(deck))
        return result
    }

}

