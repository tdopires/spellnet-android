package br.com.spellnet.model.deck

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MutableLiveDataResource
import br.com.spellnet.commom.Resource

class DeckService {

    fun deckList(): LiveDataResource<List<Deck>> = MutableLiveDataResource<List<Deck>>().apply {
        val mockedList = listOf(
            Deck(
                "edgar markov edh",
                listOf(
                    DeckSection("commander", listOf(Pair(1, Card("Edgar Markov")))),
                    DeckSection("deck", listOf(Pair(99, Card("Swamp"))))
                )
            )
        )

        postValue(Resource.Success(mockedList))
    }

}