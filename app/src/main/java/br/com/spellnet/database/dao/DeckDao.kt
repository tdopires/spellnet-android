package br.com.spellnet.database.dao

import androidx.room.*
import br.com.spellnet.database.entity.CardEntity
import br.com.spellnet.database.entity.DeckCardQuantityEntity
import br.com.spellnet.database.entity.DeckEntity
import br.com.spellnet.database.entity.DeckSectionEntity
import br.com.spellnet.database.model.DeckAndSections
import br.com.spellnet.entity.Deck

@Dao
interface DeckDao {

    @Query("SELECT * FROM card WHERE lower(name) = lower(:cardName) LIMIT 1")
    fun getCardByCardName(cardName: String): CardEntity?

    @Insert
    fun insertCard(cardEntity: CardEntity): Long

    @Transaction
    @Query("SELECT * FROM deck")
    fun getAllDecks(): List<DeckAndSections>

    @Query(
        "SELECT dcc.* " +
                "FROM deck_card_quantity dcc " +
                "INNER JOIN deck_section ds ON ds.id = dcc.deck_section_id " +
                "INNER JOIN deck d ON d.id = ds.deck_id " +
                "WHERE d.id = :deckId"
    )
    fun getDeckCardQuantityByDeckId(deckId: Long): List<DeckCardQuantityEntity>

    @Insert
    fun insertDeckCardQuantity(deckCardQuantityEntity: DeckCardQuantityEntity): Long

    @Insert
    fun insertDeckSection(deckSectionEntity: DeckSectionEntity): Long

    @Insert
    fun insertDeck(deckEntity: DeckEntity): Long

    @Transaction
    fun insertDeck(deck: Deck): Long {
        val deckId = insertDeck(DeckEntity(id = 0, name = deck.name, importUrl = deck.importUrl))
        deck.sections.forEach {
            val deckSectionId = insertDeckSection(DeckSectionEntity(id = 0, title = it.title, deckId = deckId))
            it.cardList.forEach {
                var cardId = getCardByCardName(it.card.name)?.id
                if (cardId == null) {
                    cardId = insertCard(CardEntity(name = it.card.name))
                }
                insertDeckCardQuantity(
                    DeckCardQuantityEntity(
                        id = 0,
                        quantity = it.quantity,
                        cardId = cardId,
                        deckSectionId = deckSectionId
                    )
                )
            }
        }
        return deckId
    }

    @Query("DELETE FROM deck WHERE id = :deckId")
    suspend fun deleteDeckById(deckId: Long)
}