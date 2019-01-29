package br.com.spellnet.model.deck

import br.com.spellnet.commom.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DeckRepository(private val deckService: DeckService) {

    private val mockedList = mutableListOf<Deck>()

    fun deckList(): LiveDataResource<List<Deck>> = MutableLiveDataResource<List<Deck>>().apply {
        postValue(Resource.Success(mockedList))
    }

    fun importDeck(deckImport: DeckImport): LiveDataResource<Deck> = MediatorLiveDataResource<Deck>().apply {
        runBlocking {
            val importedDeck = GlobalScope.async { deckService.importDeck(deckImport) }.await()
            if (importedDeck != null) {
                mockedList.add(importedDeck)
                postValue(Resource.Success(importedDeck))
            } else {
                postValue(Resource.Error())
            }
        }
    }


}