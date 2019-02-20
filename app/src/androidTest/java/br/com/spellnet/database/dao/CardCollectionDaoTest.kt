package br.com.spellnet.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.spellnet.database.CardDatabase
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CardCollectionDaoTest {

    private lateinit var cardCollectionDao: CardCollectionDao

    @Before
    fun initDb() {
        val cardDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            CardDatabase::class.java).build()
        cardCollectionDao = cardDatabase.cardCollectionDao()
    }

    @Test
    fun testCardQuantities() {
        Assert.assertEquals(0, cardCollectionDao.getAllCardQuantities().size)

        cardCollectionDao.upsertCardQuantities("Lightning Bolt", 3)

        Assert.assertEquals(1, cardCollectionDao.getAllCardQuantities().size)
        Assert.assertEquals(3, cardCollectionDao.getCardQuantityByCardName("Lightning Bolt")?.quantity)

        cardCollectionDao.upsertCardQuantities("Lightning Bolt", 4)
        cardCollectionDao.upsertCardQuantities("Lava Spike", 2)

        Assert.assertEquals(2, cardCollectionDao.getAllCardQuantities().size)
        Assert.assertEquals(4, cardCollectionDao.getCardQuantityByCardName("Lightning Bolt")?.quantity)
        Assert.assertEquals(2, cardCollectionDao.getCardQuantityByCardName("Lava Spike")?.quantity)

        cardCollectionDao.upsertCardQuantities("Lightning Bolt", 0)

        Assert.assertEquals(1, cardCollectionDao.getAllCardQuantities().size)
        Assert.assertEquals(2, cardCollectionDao.getCardQuantityByCardName("Lava Spike")?.quantity)
    }

}