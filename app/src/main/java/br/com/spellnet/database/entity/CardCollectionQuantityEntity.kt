package br.com.spellnet.database.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "card_collection_quantity",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("card_id"),
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("card_id", unique = true)
    ]
)
data class CardCollectionQuantityEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val quantity: Int,
    @ColumnInfo(name = "card_id") val cardId: Long
)