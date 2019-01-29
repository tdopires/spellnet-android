package br.com.spellnet

import br.com.spellnet.model.card.Card
import br.com.spellnet.model.deck.DeckParser
import br.com.spellnet.model.deck.QuantifiedCard
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DeckParserTest {

    private lateinit var deckParser: DeckParser

    @Before
    fun beforeEachTest() {
        deckParser = DeckParser()
    }

    @Test
    fun testParse_withSuccessfulInput_parseCorrectly() {
        val result = deckParser.parse("3 Arid Mesa\n" +
                "4 Bloodstained Mire\n" +
                "4 Boros Charm\n" +
                "4 Eidolon of the Great Revel\n" +
                "4 Goblin Guide\n" +
                "4 Inspiring Vantage\n" +
                "4 Lava Spike\n" +
                "4 Lightning Bolt\n" +
                "4 Lightning Helix\n" +
                "4 Monastery Swiftspear\n" +
                "3 Mountain\n" +
                "4 Rift Bolt\n" +
                "2 Sacred Foundry\n" +
                "4 Searing Blaze\n" +
                "4 Skullcrack\n" +
                "4 Wooded Foothills\n" +
                "\n" +
                "4 Chained to the Rocks\n" +
                "3 Exquisite Firecraft\n" +
                "4 Searing Blood\n" +
                "4 Smash to Smithereens\n")

        assertEquals(2, result.size)
        assertEquals(60, result[0].cardList.sumBy { it.quantity })
        assertEquals(15, result[1].cardList.sumBy { it.quantity })
        assertEquals(QuantifiedCard(3, Card("Arid Mesa")), result[0].cardList[0])
        assertEquals(QuantifiedCard(4, Card("Chained to the Rocks")), result[1].cardList[0])
    }
}
