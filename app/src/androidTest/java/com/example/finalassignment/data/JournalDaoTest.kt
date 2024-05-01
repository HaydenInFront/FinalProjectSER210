package com.example.finalassignment.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class JournalDaoTest {
    private lateinit var db: JournalRoomDatabase
    private lateinit var dao: JournalDao

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, JournalRoomDatabase::class.java).build()
        dao = db.journalDoa()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insert() = runBlocking {
        val journal = Journal(1, "title", "location", "date", "entry")
        dao.insert(journal)
        val byId = dao.getJournal(1).first()
        assertEquals(byId, journal)
    }

    @Test
    @Throws(Exception::class)
    fun update() = runBlocking {
        val journal = Journal(1, "title", "location", "date", "entry")
        dao.insert(journal)
        val updatedJournal = Journal(1, "new title", "new location", "new date", "new entry")
        dao.update(updatedJournal)
        val byId = dao.getJournal(1).first()
        assertEquals(byId, updatedJournal)
    }

    @Test
    @Throws(Exception::class)
    fun delete() = runBlocking {
        val journal = Journal(1, "title", "location", "date", "entry")
        dao.insert(journal)
        dao.delete(journal)
        val byId = dao.getJournal(1).first()
        assertEquals(byId, null)
    }

    @Test
    @Throws(Exception::class)
       fun getJournals() = runBlocking {
            val journal1 = Journal(1, "title", "location", "date", "entry")
            val journal2 = Journal(2, "title", "location", "date", "entry")
            dao.insert(journal1)
            dao.insert(journal2)
            val allJournals = dao.getJournals().first()
            assertEquals(allJournals[0], journal1)
            assertEquals(allJournals[1], journal2)
        }
}