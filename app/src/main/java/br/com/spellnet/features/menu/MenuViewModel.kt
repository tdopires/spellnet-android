package br.com.spellnet.features.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.spellnet.model.deckdetail.CardRepository
import kotlinx.coroutines.launch

class MenuViewModel(private val cardRepository: CardRepository) : ViewModel() {

    fun clearAllCardPriceCache() {
        viewModelScope.launch {
            cardRepository.clearAllCardPriceCache()
        }
    }

}