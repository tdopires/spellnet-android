package br.com.spellnet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.spellnet.database.converter.Converters
import br.com.spellnet.database.dao.CardCollectionDao
import br.com.spellnet.database.dao.CardPriceCacheDao
import br.com.spellnet.database.dao.DeckDao
import br.com.spellnet.database.entity.*

const val CARD_DATABASE_VERSION = 4

@Database(
    entities = [
        CardEntity::class,
        CardCollectionQuantityEntity::class,
        DeckEntity::class,
        DeckSectionEntity::class,
        DeckCardQuantityEntity::class,
        CardPriceCacheEntity::class
    ],
    version = CARD_DATABASE_VERSION
)
@TypeConverters(Converters::class)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cardCollectionDao(): CardCollectionDao

    abstract fun deckDao(): DeckDao

    abstract fun cardPriceCacheDao(): CardPriceCacheDao

}