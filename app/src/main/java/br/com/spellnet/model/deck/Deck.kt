package br.com.spellnet.model.deck

data class DeckImport(val name: String, val url: String)

data class Deck(val name: String, val sections: List<DeckSection>)

data class DeckSection(val title: String, val cardList: List<Pair<Int, Card>>)

data class Card(val name: String, val minPrice: Double? = null)
