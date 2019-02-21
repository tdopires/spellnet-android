package br.com.spellnet.database.entity

import androidx.room.*

@Entity(
    tableName = "deck_card_quantity",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["card_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DeckSectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["deck_section_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["card_id", "deck_section_id"], unique = true),
        Index("deck_section_id")
    ]
)
data class DeckCardQuantityEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val quantity: Int,
    @ColumnInfo(name = "card_id") val cardId: Long,
    @ColumnInfo(name = "deck_section_id") val deckSectionId: Long
)