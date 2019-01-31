package br.com.spellnet.model.deck

import br.com.spellnet.model.card.Card

class DeckParser {

    fun parse(deckString: String): List<DeckSection> {
        val deckLines = deckString.split("\n")
        val deckSections = mutableListOf<DeckSection>()
        val currentSectionCards = mutableListOf<CardQuantity>()
        deckLines.forEach {
            if (it.isNotBlank()) {
                val cardQuantityDelimiterCharIndex = it.indexOf(" ")
                if (cardQuantityDelimiterCharIndex != -1) {
                    val cardQuantity = it.substring(0, cardQuantityDelimiterCharIndex).trim()
                    val cardName = it.substring(cardQuantityDelimiterCharIndex).trim()

                    if (cardQuantity.toIntOrNull() != null) {
                        currentSectionCards.add(CardQuantity(cardQuantity.toInt(), Card(cardName)))
                    }
                }
            } else {
                deckSections.add(DeckSection(buildDeckSectionTitleFor(currentSectionCards.size), currentSectionCards.toList()))
                currentSectionCards.clear()
            }
        }
        if (currentSectionCards.size > 0) {
            deckSections.add(DeckSection(buildDeckSectionTitleFor(currentSectionCards.size), currentSectionCards.toList()))
        }
        return deckSections
    }

    //TODO remove this and get section title based on deck format (provided by some user input)
    private fun buildDeckSectionTitleFor(size: Int): String {
        return when (size) {
            15 -> "Sideboard"
            1 -> "Commander"
            else -> "Main Deck"
        }
    }
}