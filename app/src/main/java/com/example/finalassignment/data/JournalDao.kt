package com.example.finalassignment.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(journal: Journal)

    @Update
    suspend fun update(journal: Journal)

    @Delete
    suspend fun delete(journal: Journal)

    @Query("SELECT * from journal WHERE id =:id")
    fun getJournal(id: Int): Flow<Journal>

    @Query("SELECT * from journal ORDER by id ASC")
    fun getJournals(): Flow<List<Journal>>
}