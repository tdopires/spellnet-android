package br.com.spellnet.decklist.viewmodel

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.databinding.BaseObservable
import android.databinding.Bindable
import br.com.spellnet.commom.safeLet
import br.com.spellnet.model.deck.DeckBusiness
import br.com.spellnet.model.deck.DeckImport
import br.com.spellnet.BR;

class AddDeckViewModel(private val deckBusiness: DeckBusiness) : ViewModel() {

    private val deckImport = MediatorLiveData<DeckImport?>().apply { value = null }

    private val deckForm = DeckForm()

    fun onSaveDeck(deckForm: DeckForm?) {
        deckImport.value = deckForm?.toDeckImport()
    }

    fun deck() = deckForm

    fun saveDeck() = Transformations.switchMap(deckImport) {
        it?.let {
            deckBusiness.importDeck(it)
        }
    }

    class DeckForm : BaseObservable() {
        @get:Bindable
        var name: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.deckForm)
            }

        @get:Bindable
        var url: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.deckForm)
            }

    }
}

private fun AddDeckViewModel.DeckForm.toDeckImport() =
    safeLet(this.name, this.url) { name, url ->
        DeckImport(name, url)
    }
