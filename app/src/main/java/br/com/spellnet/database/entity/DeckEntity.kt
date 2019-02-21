package br.com.spellnet.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "deck"
)
data class DeckEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name: String,
    val importUrl: String
)