package br.com.spellnet.database.dao

import androidx.room.*
import br.com.spellnet.database.entity.CardCollectionQuantityEntity
import br.com.spellnet.database.entity.CardEntity
import br.com.spellnet.database.entity.CardPriceCacheEntity
import java.math.BigDecimal
import java.util.*

@Dao
interface CardPriceCacheDao {

    @Query("SELECT * FROM card WHERE lower(name) = lower(:cardName) LIMIT 1")
    fun getCardByCardName(cardName: String): CardEntity?

    @Insert
    fun insertCard(cardEntity: CardEntity): Long

    @Query(
        "SELECT cpc.* " +
                "FROM card_price_cache cpc " +
                "INNER JOIN card c ON c.id = cpc.card_id WHERE lower(c.name) = lower(:cardName) " +
                "AND cpc.lastUpdated >= (1000 * strftime('%s', datetime('now', '-2 day'))) " +
                "LIMIT 1;"
    )
    fun getCardPriceCacheByCardNameWithRefreshPeriod(cardName: String): CardPriceCacheEntity?

    @Query(
        "SELECT cpc.* " +
                "FROM card_price_cache cpc " +
                "INNER JOIN card c ON c.id = cpc.card_id WHERE lower(c.name) = lower(:cardName) " +
                "LIMIT 1;"
    )
    fun getCardPriceCacheByCardName(cardName: String): CardPriceCacheEntity?

    @Insert
    fun insertCardPriceCache(entity: CardPriceCacheEntity)

    @Update
    fun updateCardPriceCache(entity: CardPriceCacheEntity)

    @Query("DELETE FROM card_price_cache")
    suspend fun deleteAllCardPriceCache()

    @Transaction
    fun upsertCardPriceCache(
        cardName: String,
        minPrice: BigDecimal?,
        midPrice: BigDecimal?,
        maxPrice: BigDecimal?
    ) {
        val cardPriceCache = getCardPriceCacheByCardName(cardName)
        if (cardPriceCache == null) {
            var cardId = getCardByCardName(cardName)?.id
            if (cardId == null) {
                cardId = insertCard(CardEntity(name = cardName))
            }
            insertCardPriceCache(
                CardPriceCacheEntity(
                    cardId = cardId,
                    minPrice = minPrice?.toFloat(),
                    midPrice = midPrice?.toFloat(),
                    maxPrice = maxPrice?.toFloat(),
                    lastUpdated = Date()
                )
            )
        } else {
            updateCardPriceCache(
                cardPriceCache.copy(
                    minPrice = minPrice?.toFloat(),
                    midPrice = midPrice?.toFloat(),
                    maxPrice = maxPrice?.toFloat(),
                    lastUpdated = Date()
                )
            )
        }
    }

}