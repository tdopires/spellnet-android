package br.com.spellnet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.spellnet.database.dao.CardCollectionDao
import br.com.spellnet.database.entity.CardCollectionQuantityEntity
import br.com.spellnet.database.entity.CardEntity

const val CARD_DATABASE_VERSION = 1

@Database(entities = [CardEntity::class, CardCollectionQuantityEntity::class], version = CARD_DATABASE_VERSION)
abstract class CardDatabase : RoomDatabase() {

    abstract fun cardCollectionDao(): CardCollectionDao

}