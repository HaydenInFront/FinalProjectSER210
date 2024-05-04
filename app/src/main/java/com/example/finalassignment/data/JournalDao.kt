package com.example.finalassignment.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Journal database.
 * It provides methods for performing database operations on Journal objects.
 */
@Dao
interface JournalDao {

    /**
     * Inserts a new Journal into the database.
     * If a Journal with the same ID already exists, it will be ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(journal: Journal)

    /**
     * Updates an existing Journal in the database.
     */
    @Update
    suspend fun update(journal: Journal)

    /**
     * Deletes a Journal from the database.
     */
    @Delete
    suspend fun delete(journal: Journal)

    /**
     * Retrieves a Journal from the database by its ID.
     * Returns a Flow that emits the Journal when it changes.
     */
    @Query("SELECT * from journal WHERE id =:id")
    fun getJournal(id: Int): Flow<Journal>

    /**
     * Retrieves all Journals from the database, ordered by their ID in ascending order.
     * Returns a Flow that emits the list of Journals when it changes.
     */
    @Query("SELECT * from journal ORDER by id ASC")
    fun getJournals(): Flow<List<Journal>>
}