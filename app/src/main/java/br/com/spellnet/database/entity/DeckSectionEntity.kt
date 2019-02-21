package br.com.spellnet.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "deck_section",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deck_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DeckSectionEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val title: String,
    @ColumnInfo(name = "deck_id") val deckId: Long
)