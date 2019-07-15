package br.com.spellnet.model.decklist

import br.com.spellnet.entity.Card
import br.com.spellnet.entity.CardQuantity
import br.com.spellnet.entity.DeckSection

class DeckParser {

    @Throws(DeckParseException::class)
    fun parse(deckString: String?): List<DeckSection> {
        if (deckString.isNullOrBlank()) throw DeckParseException()

        val normalizedDeckString = normalizeDecFileStrings(deckString)

        val deckLines = normalizedDeckString.split("\n")
        val deckSections = mutableListOf<DeckSection>()
        val currentSectionCards = mutableListOf<CardQuantity>()
        deckLines.forEach { line ->
            if (line.isNotBlank()) {
                val numberRegex = Regex("([0-9]*)")
                val matchResult = numberRegex.find(line, 0)
                matchResult?.let { match ->
                    val cardQuantity = match.value.trim()
                    val cardName = line.replaceFirst(numberRegex, "").trim()

                    if (cardQuantity.toIntOrNull() != null) {
                        currentSectionCards.add(
                            CardQuantity(
                                cardQuantity.toInt(),
                                Card(cardName)
                            )
                        )
                    }
                }
            } else {
                deckSections.add(
                    DeckSection(
                        buildDeckSectionTitleFor(currentSectionCards),
                        currentSectionCards.toList()
                    )
                )
                currentSectionCards.clear()
            }
        }
        if (currentSectionCards.size > 0) {
            deckSections.add(
                DeckSection(
                    buildDeckSectionTitleFor(currentSectionCards),
                    currentSectionCards.toList()
                )
            )
        }
        return deckSections
    }

    private fun normalizeDecFileStrings(deckString: String): String {
        val deckLines = if (deckString[deckString.length - 1] == '\n') {
            deckString.substring(0, deckString.length - 1)
        } else {
            deckString
        }.split("\n")
        val normalizedDeckLines = StringBuilder()

        var changedDeckSection = false
        deckLines.forEach {
            if (it.startsWith("//")) {
                return@forEach
            }
            val cardNameWithoutEdition = it.replace(Regex("""(\[.*\])"""), "").replace("/", " // ")

            if ((cardNameWithoutEdition.startsWith("SB:")) && !changedDeckSection) {
                changedDeckSection = true
                normalizedDeckLines.append("\n")
            }
            normalizedDeckLines.append(cardNameWithoutEdition.replace("SB:", "").trim() + "\n")
        }
        return normalizedDeckLines.substring(0, normalizedDeckLines.length - 1).toString()
    }

    //TODO remove this and get section title based on deck format (provided by some user input)
    private fun buildDeckSectionTitleFor(cardQuantities: List<CardQuantity>): String {
        return when (cardQuantities.sumBy { it.quantity }) {
            15 -> "Sideboard"
            1 -> "Commander"
            else -> "Main Deck"
        }
    }
}

class DeckParseException : Exception()