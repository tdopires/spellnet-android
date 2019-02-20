package br.com.spellnet.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "card",
    indices = [
        Index("name", unique = true)
    ]
)
data class CardEntity(@PrimaryKey(autoGenerate = true) var id: Long = 0, val name: String)