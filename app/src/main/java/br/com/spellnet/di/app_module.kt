package br.com.spellnet.di

import br.com.spellnet.decklist.viewmodel.DeckListViewModel
import br.com.spellnet.model.deck.DeckBusiness
import br.com.spellnet.model.deck.DeckRepository
import br.com.spellnet.model.deck.DeckService
import org.koin.dsl.module.module

val deckModule = module {
    factory { DeckListViewModel(get()) }
    factory { DeckBusiness(get()) }
    factory { DeckRepository(get()) }
    factory { DeckService() }
    //factory { DeckDao() }
}

// Gather all app modules
val spellnetApp = listOf(deckModule)
