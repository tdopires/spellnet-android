package br.com.spellnet

import br.com.spellnet.entity.Card
import br.com.spellnet.model.decklist.DeckParser
import br.com.spellnet.entity.CardQuantity
import br.com.spellnet.model.decklist.DeckParseException
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DeckParserTest {

    private lateinit var deckParser: DeckParser

    @Before
    fun beforeEachTest() {
        deckParser = DeckParser()
    }

    @Test
    fun testParse_withSuccessfulDefaultInput_parseCorrectly() {
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
        assertEquals(CardQuantity(3, Card("Arid Mesa")), result[0].cardList[0])
        assertEquals(CardQuantity(4, Card("Goblin Guide")), result[0].cardList[4])
        assertEquals(CardQuantity(4, Card("Chained to the Rocks")), result[1].cardList[0])
    }

    @Test
    fun testParse_withSuccessfulMtgTop8Input_parseCorrectly() {
        val result = deckParser.parse("// Deck file created with mtgtop8.com\n" +
                "// NAME : Infect\n" +
                "// CREATOR : Peter_Thomsen\n" +
                "// FORMAT : Modern\n" +
                "4 [CFX] Noble Hierarch\n" +
                "4 [NPH] Glistener Elf\n" +
                "4 [NPH] Blighted Agent\n" +
                "4 [ZEN] Vines of Vastwood\n" +
                "4 [TSP] Might of Old Krosa\n" +
                "4 [NPH] Mutagenic Growth\n" +
                "3 [KLD] Blossoming Defense\n" +
                "3 [KTK] Become Immense\n" +
                "2 [WWK] Groundswell\n" +
                "2 [ROE] Distortion Strike\n" +
                "1 [M13] Rancor\n" +
                "1 [NPH] Dismember\n" +
                "1 [PLC] Piracy Charm\n" +
                "1 [SOM] Twisted Image\n" +
                "1 [XLN] Spell Pierce\n" +
                "1 [NPH] Spellskite\n" +
                "4 [ZEN] Misty Rainforest\n" +
                "4 [MBS] Inkmoth Nexus\n" +
                "3 [KTK] Windswept Heath\n" +
                "2 [RTR] Forest\n" +
                "2 [] Breeding Pool\n" +
                "2 [TSP] Pendelhaven\n" +
                "1 [KLD] Botanical Sanctum\n" +
                "1 [KTK] Wooded Foothills\n" +
                "1 [FUT] Dryad Arbor\n" +
                "SB:  1 [NPH] Surgical Extraction\n" +
                "SB:  1 [KLD] Ceremonious Rejection\n" +
                "SB:  1 [AVR] Wild Defiance\n" +
                "SB:  1 [BFZ] Dispel\n" +
                "SB:  1 [ROE] Distortion Strike\n" +
                "SB:  1 [NPH] Dismember\n" +
                "SB:  1 [XLN] Spell Pierce\n" +
                "SB:  1 [NPH] Spellskite\n" +
                "SB:  2 [CNS] Nature's Claim\n" +
                "SB:  2 [SHM] Kitchen Finks\n" +
                "SB:  2 [OGW] Nissa, Voice of Zendikar\n" +
                "SB:  1 [ALA] Relic of Progenitus\n")

        assertEquals(2, result.size)
        assertEquals(60, result[0].cardList.sumBy { it.quantity })
        assertEquals(15, result[1].cardList.sumBy { it.quantity })
        assertEquals(CardQuantity(4, Card("Noble Hierarch")), result[0].cardList[0])
        assertEquals(CardQuantity(1, Card("Surgical Extraction")), result[1].cardList[0])
    }

    @Test
    fun testParse_withSomeMessedUpSplitInput_parseCorrectly() {
        val result = deckParser.parse("3 Arid Mesa\n" +
                "4 Bloodstained Mire\n" +
                "4 Boros Charm\n" +
                "4 Eidolon of the Great Revel\n" +
                "2 Goblin Guide\n" +
                "2 Goblin Guide\n" +
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
                "1 Chained to the Rocks\n" +
                "3 Chained to the Rocks\n" +
                "3 Exquisite Firecraft\n" +
                "4 Searing Blood\n" +
                "4 Smash to Smithereens\n")

        assertEquals(2, result.size)
        assertEquals(60, result[0].cardList.sumBy { it.quantity })
        assertEquals(15, result[1].cardList.sumBy { it.quantity })
        assertEquals(CardQuantity(3, Card("Arid Mesa")), result[0].cardList[0])
        assertEquals(CardQuantity(4, Card("Goblin Guide")), result[0].cardList[4])
        assertEquals(CardQuantity(4, Card("Chained to the Rocks")), result[1].cardList[0])
    }

    @Test(expected = DeckParseException::class)
    fun testParse_withNullInput_parseCorrectly() {
        deckParser.parse(null)
    }
}
