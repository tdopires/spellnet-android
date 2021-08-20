package br.com.spellnet.database.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import br.com.spellnet.entity.Card
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = "card_price_cache",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["card_id"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("card_id", unique = true)
    ]
)
data class CardPriceCacheEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "card_id") val cardId: Long,
    val minPrice: Float?,
    val midPrice: Float?,
    val maxPrice: Float?,
    val lastUpdated: Date
)