package com.example.finalassignment

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.example.finalassignment.data.Journal
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertEquals

class JournalAdapterTest {
    private var adapter: JournalAdapter? = null
    private var recyclerView: RecyclerView? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        recyclerView = RecyclerView(context)
        adapter = JournalAdapter { }
        runOnUiThread {
            recyclerView!!.adapter = adapter
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        adapter = null
        recyclerView = null
    }

    @Test
    @Throws(Exception::class)
    fun testJournalAdapter() {
        val journal = Journal(1, "title", "location", "date", "entry")
        runOnUiThread {
            adapter!!.submitList(listOf(journal))
        }
        val item = adapter!!.currentList[0]
        assertEquals(item, journal)
    }
}