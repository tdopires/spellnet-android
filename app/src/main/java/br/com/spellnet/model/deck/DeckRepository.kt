package br.com.spellnet.model.deck

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import br.com.spellnet.commom.LiveDataResource

class DeckRepository(private val deckService: DeckService) {

    fun deckList(): LiveDataResource<List<Deck>> = deckService.deckList()

}