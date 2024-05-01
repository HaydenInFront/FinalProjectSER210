package com.example.finalassignment

import android.util.Log
import androidx.lifecycle.*
import com.example.finalassignment.data.Journal
import com.example.finalassignment.data.JournalDao
import kotlinx.coroutines.launch

class JournalViewModel(private val journalDao: JournalDao): ViewModel() {

    val allJournals: LiveData<List<Journal>> = journalDao.getJournals().asLiveData()

    fun addNewJournal(journalTitle: String, journalLocation: String, journalDate: String, journalEntry: String) {
        val newJournal = getNewJournalEntry(journalTitle, journalLocation, journalDate, journalEntry)
        insertJournal(newJournal)
    }

    private fun insertJournal(newJournal: Journal){
        viewModelScope.launch {
            val journals = journalDao.insert(newJournal)
            Log.d("JournalViewModel", "Loaded journals: $journals")
        }
    }

    fun retrieveJournal(id: Int): LiveData<Journal> {
        return journalDao.getJournal(id).asLiveData()
    }

    private fun getNewJournalEntry(journalTitle: String, journalLocation: String, journalDate: String, journalEntry: String): Journal {
        return (
            Journal(
                title = journalTitle,
                location = journalLocation,
                date = journalDate,
                entry = journalEntry
            ))
    }

    fun isEntryValid(journalTitle: String, journalLocation: String, journalDate: String, journalEntry: String): Boolean {
        return journalTitle.isNotBlank() && journalLocation.isNotBlank() && journalDate.isNotBlank() && journalEntry.isNotBlank()
    }

    fun deleteJournal(journal: Journal) {
        viewModelScope.launch {
            journalDao.delete(journal)
        }
    }

    fun updateJournal(journal: Journal) {
        viewModelScope.launch {
            journalDao.update(journal)
        }
    }
}
class JournalViewModelFactory(private val journalDao: JournalDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(journalDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
