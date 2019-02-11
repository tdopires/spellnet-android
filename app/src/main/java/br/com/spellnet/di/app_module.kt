package br.com.spellnet.di

import br.com.spellnet.features.deckdetail.viewmodel.DeckDetailViewModel
import br.com.spellnet.features.decklist.viewmodel.AddDeckViewModel
import br.com.spellnet.features.decklist.viewmodel.DeckListViewModel
import br.com.spellnet.model.cardcollection.CardCollectionBusiness
import br.com.spellnet.model.cardcollection.CardCollectionRepository
import br.com.spellnet.model.deckdetail.CardBusiness
import br.com.spellnet.model.deckdetail.CardRepository
import br.com.spellnet.model.deckdetail.CardService
import br.com.spellnet.model.decklist.DeckBusiness
import br.com.spellnet.model.decklist.DeckParser
import br.com.spellnet.model.decklist.DeckRepository
import br.com.spellnet.model.decklist.DeckService
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

//TODO change to multiple modules later
val module = module {
    viewModel { DeckListViewModel(get()) }
    viewModel { AddDeckViewModel(get()) }
    viewModel { DeckDetailViewModel(get(), get()) }

    factory { DeckBusiness(get()) }
    factory { CardBusiness(get()) }
    factory { CardCollectionBusiness(get()) }

    single { DeckRepository(get()) } //TODO change to factory later (when room database implemented)
    factory { CardRepository(get()) }
    single { CardCollectionRepository() } //TODO change to factory later (when room database implemented)

    factory { DeckService(get()) }
    factory { CardService() }

    factory { DeckParser() }

    //TODO implement database (room)
    //factory { DeckDao() }
}

// Gather all app modules
val spellnetApp = listOf(module)
