package br.com.spellnet.model.deck

import br.com.spellnet.model.card.Card

class DeckParser {

    fun parse(deckString: String): List<DeckSection> {
        val deckLines = deckString.split("\n")
        val deckSections = mutableListOf<DeckSection>()
        val currentSectionCards = mutableListOf<QuantifiedCard>()
        deckLines.forEach {
            if (it.isNotBlank()) {
                val cardQuantityDelimiterCharIndex = it.indexOf(" ")
                if (cardQuantityDelimiterCharIndex != -1) {
                    val cardQuantity = it.substring(0, cardQuantityDelimiterCharIndex).trim()
                    val cardName = it.substring(cardQuantityDelimiterCharIndex).trim()

                    if (cardQuantity.toIntOrNull() != null) {
                        currentSectionCards.add(QuantifiedCard(cardQuantity.toInt(), Card(cardName)))
                    }
                }
            } else {
                deckSections.add(DeckSection("", currentSectionCards.toList()))
                currentSectionCards.clear()
            }
        }
        if (currentSectionCards.size > 0) {
            deckSections.add(DeckSection("", currentSectionCards.toList()))
        }
        return deckSections
    }
}