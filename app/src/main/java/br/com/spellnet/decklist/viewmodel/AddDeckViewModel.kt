package br.com.spellnet.decklist.viewmodel

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import br.com.spellnet.commom.safeLet
import br.com.spellnet.model.deck.DeckBusiness
import br.com.spellnet.model.deck.DeckImport

class AddDeckViewModel(private val deckBusiness: DeckBusiness) : ViewModel() {

    private val deckImport = MediatorLiveData<DeckImport>().apply { value = null }

    private val deckForm = DeckForm()

    fun onSaveDeck() {
        deckImport.value = deckForm.toDeckImport()
    }

    fun deck() = deckForm

    fun saveDeck() = Transformations.switchMap(deckImport) {
        deckBusiness.importDeck(it)
    }

    data class DeckForm(var name: String? = null, var url: String? = null)

}

private fun AddDeckViewModel.DeckForm.toDeckImport() =
    safeLet(this.name, this.url) { name, url ->
        DeckImport(name, url)
    }
