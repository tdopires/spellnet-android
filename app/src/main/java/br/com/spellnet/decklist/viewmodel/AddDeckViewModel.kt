package br.com.spellnet.decklist.viewmodel

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import br.com.spellnet.decklist.view.AddDeckForm
import br.com.spellnet.decklist.view.toDeckImport
import br.com.spellnet.model.deck.DeckBusiness
import br.com.spellnet.model.deck.DeckImport

class AddDeckViewModel(private val deckBusiness: DeckBusiness) : ViewModel() {

    private val deckImport = MediatorLiveData<DeckImport?>().apply { value = null }

    fun onSaveDeck(addDeckForm: AddDeckForm?) {
        deckImport.value = addDeckForm?.toDeckImport()
    }

    fun saveDeck() = Transformations.switchMap(deckImport) {
        it?.let {
            deckBusiness.importDeck(it)
        }
    }

}
