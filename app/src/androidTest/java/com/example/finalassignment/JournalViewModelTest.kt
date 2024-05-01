package com.example.finalassignment

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.finalassignment.data.Journal
import com.example.finalassignment.data.JournalDao
import com.example.finalassignment.data.JournalRoomDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.assertEquals

class JournalViewModelTest {
    private lateinit var db: JournalRoomDatabase
    private lateinit var dao: JournalDao
    private lateinit var viewModel: JournalViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, JournalRoomDatabase::class.java).build()
        dao = db.journalDoa()
        viewModel = JournalViewModel(dao)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun getJournals() {
        viewModel.addNewJournal("title", "location", "date", "entry")
        val journal = Journal(1, "title", "location", "date", "entry")
        val allJournals = viewModel.allJournals.value
        assertEquals(journal, allJournals?.get(0))
        viewModel.deleteJournal(journal)
    }

    @Test
    @Throws(Exception::class)
    fun addNewJournal() = runBlocking {
        viewModel.addNewJournal("title", "location", "date", "entry")
        val journal = Journal(1, "title", "location", "date", "entry")
        val byId = dao.getJournal(1).first()
        assertEquals(byId, journal)
        viewModel.deleteJournal(byId)
    }

    @Test
    @Throws(Exception::class)
    fun retrieveJournal() {
        viewModel.addNewJournal("title", "location", "date", "entry")
        val journal = Journal(1, "title", "location", "date", "entry")
        val byId = viewModel.retrieveJournal(1).value
        assertEquals(journal, byId)
    }

    @Test
    @Throws(Exception::class)
    fun isEntryValid() {
        var result = viewModel.isEntryValid("title", "location", "date", "entry")
        assertEquals(result, true)
        result = viewModel.isEntryValid("", "location", "date", "entry")
        assertEquals(result, false)
    }

    @Test
    @Throws(Exception::class)
    fun deleteJournal() = runBlocking {
        viewModel.addNewJournal("title", "location", "date", "entry")
        val journal = Journal(1, "title", "location", "date", "entry")
        viewModel.deleteJournal(journal)
        val byId = viewModel.retrieveJournal(1).value
        assertEquals(byId, null)
    }

    @Test
    @Throws(Exception::class)
    fun updateJournal() = runBlocking {
        val newTitle = "newTitle"
        viewModel.addNewJournal("title", "location", "date", "entry")
        val journal = Journal(1, newTitle, "location", "date", "entry")
        viewModel.updateJournal(journal)
        val byId = viewModel.retrieveJournal(1).value
        assertEquals(newTitle, byId?.title)
    }
}