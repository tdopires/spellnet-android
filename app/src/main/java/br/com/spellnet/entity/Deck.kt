package br.com.spellnet.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

data class DeckImport(
    val name: String,
    val url: String
)

@Parcelize
data class Deck(
    var entityId: Long? = null,
    val name: String,
    val importUrl: String,
    val sections: List<DeckSection>
) : Parcelable

@Parcelize
data class DeckSection(
    val title: String,
    val cardList: List<CardQuantity>
) : Parcelable

@Parcelize
data class CardQuantity(
    val quantity: Int,
    val card: Card
) : Parcelable

// Helper extensions

fun Deck.fullFlatCardsList(): List<Card> =
    this.sections.flatMap { it.cardList.map { it.card } }
        .distinctBy { it.name.toLowerCase(Locale.US) }