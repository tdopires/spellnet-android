package br.com.spellnet.database.entity

import androidx.room.*

@Entity(
    tableName = "deck_section",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deck_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("deck_id")
    ]
)
data class DeckSectionEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val title: String,
    @ColumnInfo(name = "deck_id") val deckId: Long
)