package com.example.finalassignment.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Test

class JournalRoomDatabaseTest {
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
    fun readAndWrite() = runBlocking {
        val journal = Journal(1, "title", "location", "date", "entry")
        dao.insert(journal)
        val byId = dao.getJournal(1).first()
        assertEquals(byId, journal)
    }
}