package com.example.finalassignment.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for the Journal data.
 * It provides a singleton instance of the database, which is used to access the data.
 */
@Database(entities = [Journal::class], version = 1, exportSchema = false)
abstract class JournalRoomDatabase: RoomDatabase() {

    /**
     * Provides access to the JournalDao for database operations.
     */
    abstract fun journalDoa(): JournalDao

    companion object {
        /**
         * Singleton instance of the JournalRoomDatabase.
         */
        @Volatile
        private var INSTANCE: JournalRoomDatabase? = null

        /**
         * Returns the singleton instance of the JournalRoomDatabase.
         * If the instance is not already created, it creates a new one.
         */
        fun getDatabase(context: Context): JournalRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JournalRoomDatabase::class.java,
                    "journal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}