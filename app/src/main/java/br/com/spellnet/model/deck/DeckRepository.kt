package br.com.spellnet.model.deck

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MutableLiveDataResource
import br.com.spellnet.commom.Resource

class DeckRepository(private val deckService: DeckService) {

    private val mockedList = mutableListOf<Deck>()

    fun deckList(): LiveDataResource<List<Deck>> = MutableLiveDataResource<List<Deck>>().apply {
        postValue(Resource.Success(mockedList))
    }

    fun addDeck(deck: Deck): Boolean {
        mockedList.add(deck)
        return true
    }

}