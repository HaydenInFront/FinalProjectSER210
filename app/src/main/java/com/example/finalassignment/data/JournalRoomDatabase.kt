package com.example.finalassignment.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Journal::class], version = 1, exportSchema = false)
abstract class JournalRoomDatabase: RoomDatabase() {

    abstract fun journalDoa(): JournalDao

    companion object {
        @Volatile
        private var INSTANCE: JournalRoomDatabase? = null

        fun getDatabase(context: Context): JournalRoomDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext.applicationContext,
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