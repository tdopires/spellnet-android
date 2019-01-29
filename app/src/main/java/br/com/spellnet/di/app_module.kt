package br.com.spellnet.di

import br.com.spellnet.decklist.viewmodel.AddDeckViewModel
import br.com.spellnet.decklist.viewmodel.DeckDetailViewModel
import br.com.spellnet.decklist.viewmodel.DeckListViewModel
import br.com.spellnet.model.card.CardBusiness
import br.com.spellnet.model.card.CardRepository
import br.com.spellnet.model.card.CardService
import br.com.spellnet.model.deck.DeckBusiness
import br.com.spellnet.model.deck.DeckParser
import br.com.spellnet.model.deck.DeckRepository
import br.com.spellnet.model.deck.DeckService
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

//TODO change to multiple modules later
val module = module {
    viewModel { DeckListViewModel(get()) }
    viewModel { AddDeckViewModel(get()) }
    viewModel { DeckDetailViewModel(get()) }

    factory { DeckBusiness(get()) }
    factory { CardBusiness(get()) }

    //TODO change to factory later (when decks database implemented)
    single { DeckRepository(get()) }
    factory { CardRepository(get()) }

    factory { DeckService(get()) }
    factory { CardService() }

    factory { DeckParser() }

    //TODO implement database (room)
    //factory { DeckDao() }
}

// Gather all app modules
val spellnetApp = listOf(module)
