package br.com.spellnet.features.decklist.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.spellnet.commom.SingleLiveEvent
import br.com.spellnet.entity.Deck
import br.com.spellnet.model.decklist.DeckBusiness
import kotlinx.coroutines.launch

class DeckListViewModel(private val deckBusiness: DeckBusiness) : ViewModel() {

    open class Action {
        class AddDeck(val deckUrlToImport: String?) : Action()
        class OpenDeck(val deck: Deck) : Action()
        class DeleteDeck(val deck: Deck) : Action()
    }

    private val action = SingleLiveEvent<Action>()

    private val retryDeckList = SingleLiveEvent<Boolean>().apply { value = true }
    private val deckList = Transformations.switchMap(retryDeckList) {
        deckBusiness.deckList()
    }

    fun addDeck(deckUrlToImport: String? = null) {
        action.value = Action.AddDeck(deckUrlToImport)
    }

    fun retryDeckList() {
        retryDeckList.call()
    }

    fun openDeck(deck: Deck) {
        action.value = Action.OpenDeck(deck)
    }

    fun deleteDeck(deck: Deck) {
        action.value = Action.DeleteDeck(deck)
    }

    fun confirmDeleteDeck(deck: Deck) {
        viewModelScope.launch {
            deckBusiness.deleteDeck(deck)
            retryDeckList.call()
        }
    }

    fun action() = action
    fun deckList() = deckList

}
