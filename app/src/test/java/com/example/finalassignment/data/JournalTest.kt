package com.example.finalassignment.data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class JournalTest {

    private var mJournal: Journal? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mJournal = Journal()
    }

    @Test
    @Throws(Exception::class)
    fun getId() {
        assertEquals(0, mJournal!!.id)
    }

    @Test
    @Throws(Exception::class)
    fun getTitle() {
        assertEquals("", mJournal!!.title)
    }

    @Test
    @Throws(Exception::class)
    fun getLocation() {
        assertEquals("", mJournal!!.location)
    }

    @Test
    @Throws(Exception::class)
    fun getDate() {
        assertEquals("", mJournal!!.date)
    }

    @Test
    @Throws(Exception::class)
    fun getEntry() {
        assertEquals("", mJournal!!.entry)
    }
}