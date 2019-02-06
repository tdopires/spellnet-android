package br.com.spellnet.decklist.viewmodel

import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import br.com.spellnet.model.deck.Deck
import br.com.spellnet.model.deck.DeckBusiness
import com.example.android.architecture.blueprints.todoapp.SingleLiveEvent

class DeckListViewModel(private val deckBusiness: DeckBusiness) : ViewModel() {

    open class Action {
        class AddDeck(val deckUrlToImport: String?) : Action()
        class OpenDeck(val deck: Deck) : Action()
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

    fun action() = action
    fun deckList() = deckList

}
