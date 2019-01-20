package br.com.spellnet.model.deck

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MediatorLiveDataResource
import br.com.spellnet.commom.MutableLiveDataResource
import br.com.spellnet.commom.Resource
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request


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

    suspend fun importDeck(deckImport: DeckImport): Deck? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://www.mtggoldfish.com/deck/download/1555036")
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body()

            return Deck(deckImport.name, listOf())
        }
        return null
    }

}