package br.com.spellnet.decklist.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.spellnet.model.deck.Card
import br.com.spellnet.model.deck.Deck
import br.com.spellnet.model.deck.DeckSection

class DeckListViewModel : ViewModel() {

    open class Action {
        object AddDeck: Action()
        class OpenDeck(val deck: Deck): Action()
    }

    private val action = MutableLiveData<Action>()
    private val deckList = MutableLiveData<List<Deck>>()

    fun addDeck() {
        //action.value = Action.AddDeck
        deckList.value = (deckList.value ?: emptyList()).toMutableList().apply {
            add(Deck("edgar markov edh",
                listOf(
                    DeckSection("commander", listOf(Pair(1, Card("Edgar Markov")))),
                    DeckSection("deck", listOf(Pair(99, Card("Swamp"))))
                )
            ))
        }
    }

    fun openDeck(deck: Deck) {
        action.value = Action.OpenDeck(deck)
    }

    fun action() = action
    fun deckList() = deckList

}
