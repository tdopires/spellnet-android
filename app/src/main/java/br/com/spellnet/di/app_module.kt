package br.com.spellnet.di

import br.com.spellnet.decklist.viewmodel.AddDeckViewModel
import br.com.spellnet.decklist.viewmodel.DeckListViewModel
import br.com.spellnet.model.deck.DeckBusiness
import br.com.spellnet.model.deck.DeckRepository
import br.com.spellnet.model.deck.DeckService
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val deckModule = module {
    viewModel { DeckListViewModel(get()) }
    viewModel { AddDeckViewModel(get()) }

    factory { DeckBusiness(get()) }

    //TODO change to factory
    single { DeckRepository(get()) }

    factory { DeckService() }
    //factory { DeckDao() }
}

// Gather all app modules
val spellnetApp = listOf(deckModule)
