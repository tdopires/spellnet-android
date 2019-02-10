package br.com.spellnet.features.decklist.viewmodel

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import br.com.spellnet.features.decklist.view.AddDeckForm
import br.com.spellnet.features.decklist.view.toDeckImport
import br.com.spellnet.model.decklist.DeckBusiness
import br.com.spellnet.entity.DeckImport

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
