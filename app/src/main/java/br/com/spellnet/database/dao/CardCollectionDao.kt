package br.com.spellnet.database.dao

import androidx.room.*
import br.com.spellnet.database.entity.CardCollectionQuantityEntity
import br.com.spellnet.database.entity.CardEntity

@Dao
interface CardCollectionDao {

    @Query("SELECT * FROM card_collection_quantity")
    fun getAllCardQuantities(): List<CardCollectionQuantityEntity>

    @Query(
        "SELECT ccq.* " +
                "FROM card_collection_quantity ccq " +
                "INNER JOIN card c ON c.id = ccq.card_id WHERE lower(c.name) = lower(:cardName) " +
                "LIMIT 1"
    )
    fun getCardQuantityByCardName(cardName: String): CardCollectionQuantityEntity?

    @Query("SELECT * FROM card WHERE lower(name) = lower(:cardName) LIMIT 1")
    fun getCardByCardName(cardName: String): CardEntity?

    @Insert
    fun insertCard(cardEntity: CardEntity): Long

    @Insert
    fun insertCardCollectionQuantity(cardCollectionQuantityEntity: CardCollectionQuantityEntity)

    @Update
    fun updateCardCollectionQuantity(cardCollectionQuantityEntity: CardCollectionQuantityEntity)

    @Delete
    fun deleteCardCollectionQuantity(cardCollectionQuantityEntity: CardCollectionQuantityEntity)

    @Transaction
    fun upsertCardQuantities(cardName: String, quantity: Int) {
        val cardCollectionQuantity = getCardQuantityByCardName(cardName)
        if (cardCollectionQuantity == null) {
            var cardId = getCardByCardName(cardName)?.id
            if (cardId == null) {
                cardId = insertCard(CardEntity(name = cardName))
            }
            insertCardCollectionQuantity(CardCollectionQuantityEntity(quantity = quantity, cardId = cardId))
        } else {
            if (quantity == 0) {
                deleteCardCollectionQuantity(cardCollectionQuantity)
            } else {
                updateCardCollectionQuantity(cardCollectionQuantity.copy(quantity = quantity))
            }
        }
    }

}