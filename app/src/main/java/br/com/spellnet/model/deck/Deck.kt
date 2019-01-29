package br.com.spellnet.model.deck

import android.os.Parcelable
import br.com.spellnet.model.card.Card
import kotlinx.android.parcel.Parcelize

data class DeckImport(val name: String, val url: String)

@Parcelize
data class Deck(val name: String, val sections: List<DeckSection>) : Parcelable

@Parcelize
data class DeckSection(val title: String, val cardList: List<QuantifiedCard>) : Parcelable

@Parcelize
data class QuantifiedCard(val quantity: Int, val card: Card) : Parcelable

// Helper extensions

fun Deck.fullFlatCardsList(): List<Card> = this.sections.flatMap { it.cardList.map { it.card } }