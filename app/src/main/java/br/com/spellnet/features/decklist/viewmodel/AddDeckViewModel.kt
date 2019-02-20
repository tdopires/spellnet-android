package br.com.spellnet.features.decklist.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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
