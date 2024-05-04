package com.example.finalassignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.finalassignment.data.Journal
import com.example.finalassignment.data.JournalDao
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the data related to Journals.
 * @property journalDao DAO for accessing Journal data.
 */
class JournalViewModel(private val journalDao: JournalDao): ViewModel() {

    /**
     * LiveData object for observing changes to all Journals.
     */
    val allJournals: LiveData<List<Journal>> = journalDao.getJournals().asLiveData()

    /**
     * Adds a new Journal entry.
     */
    fun addNewJournal(journalTitle: String, journalLocation: String, journalDate: String, journalEntry: String) {
        val newJournal = getNewJournalEntry(journalTitle, journalLocation, journalDate, journalEntry)
        insertJournal(newJournal)
    }

    /**
     * Inserts a new Journal entry into the database.
     */
    private fun insertJournal(newJournal: Journal){
        viewModelScope.launch {
            val journals = journalDao.insert(newJournal)
            Log.d("JournalViewModel", "Loaded journals: $journals")
        }
    }

    /**
     * Retrieves a Journal entry by its ID.
     */
    fun retrieveJournal(id: Int): LiveData<Journal> {
        return journalDao.getJournal(id).asLiveData()
    }

    /**
     * Creates a new Journal entry.
     */
    private fun getNewJournalEntry(journalTitle: String, journalLocation: String, journalDate: String, journalEntry: String): Journal {
        return (
                Journal(
                    title = journalTitle,
                    location = journalLocation,
                    date = journalDate,
                    entry = journalEntry
                ))
    }

    /**
     * Checks if a Journal entry is valid.
     */
    fun isEntryValid(journalTitle: String, journalLocation: String, journalDate: String, journalEntry: String): Boolean {
        return journalTitle.isNotBlank() && journalLocation.isNotBlank() && journalDate.isNotBlank() && journalEntry.isNotBlank()
    }

    /**
     * Deletes a Journal entry.
     */
    fun deleteJournal(journal: Journal) {
        viewModelScope.launch {
            journalDao.delete(journal)
        }
    }

    /**
     * Updates a Journal entry.
     */
    fun updateJournal(journal: Journal) {
        viewModelScope.launch {
            journalDao.update(journal)
        }
    }
}

/**
 * Factory for creating JournalViewModel instances.
 * @property journalDao DAO for accessing Journal data.
 */
class JournalViewModelFactory(private val journalDao: JournalDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(journalDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}