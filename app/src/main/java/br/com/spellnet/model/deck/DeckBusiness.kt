package br.com.spellnet.model.deck

import br.com.spellnet.commom.LiveDataResource
import br.com.spellnet.commom.MutableLiveDataResource
import br.com.spellnet.commom.Resource

class DeckBusiness(private val deckRepository: DeckRepository) {

    fun deckList(): LiveDataResource<List<Deck>> = deckRepository.deckList()

    fun importDeck(deckImport: DeckImport): LiveDataResource<Deck> {
        val result = MutableLiveDataResource<Deck>()
        result.postValue(Resource.Success(Deck(deckImport.name, listOf())))
        return result
    }

}