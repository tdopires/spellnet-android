package br.com.spellnet.model.decklist

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.MutableLiveDataResource
import br.com.spellnet.commom.Resource
import br.com.spellnet.entity.Deck
import br.com.spellnet.entity.DeckImport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DeckRepository(private val deckService: DeckService) {

    private val mockedList = mutableListOf<Deck>()

    fun deckList(): LiveDataResource<List<Deck>> = MutableLiveDataResource<List<Deck>>().apply {
        postValue(Resource.Success(mockedList))
    }

    fun importDeck(deckImport: DeckImport): LiveDataResource<Deck> = MediatorLiveDataResource<Deck>().apply {
        GlobalScope.launch {
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