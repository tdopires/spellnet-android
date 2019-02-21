package br.com.spellnet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.spellnet.database.dao.CardCollectionDao
import br.com.spellnet.database.dao.DeckDao
import br.com.spellnet.database.entity.*

const val CARD_DATABASE_VERSION = 2

@Database(
    entities = [
        CardEntity::class,
        CardCollectionQuantityEntity::class,
        DeckEntity::class,
        DeckSectionEntity::class,
        DeckCardQuantityEntity::class
    ],
    version = CARD_DATABASE_VERSION
)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cardCollectionDao(): CardCollectionDao

    abstract fun deckDao(): DeckDao

}