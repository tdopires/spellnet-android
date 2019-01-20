package br.com.spellnet.model.deck

import br.com.spellnet.commom.*

class DeckRepository(private val deckService: DeckService) {

    private val mockedList = mutableListOf<Deck>()

    fun deckList(): LiveDataResource<List<Deck>> = MutableLiveDataResource<List<Deck>>().apply {
        postValue(Resource.Success(mockedList))
    }

    fun importDeck(deckImport: DeckImport): LiveDataResource<Deck> = MediatorLiveDataResource<Deck>().apply {
        la
        val importedDeck = deckService.importDeck(deckImport)
        if (importedDeck != null) {
            mockedList.add(importedDeck)
            postValue(Resource.Success(importedDeck))
        } else {
            postValue(Resource.Error())
        }
    }


}