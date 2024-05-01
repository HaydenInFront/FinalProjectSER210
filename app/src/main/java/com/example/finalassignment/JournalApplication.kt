package com.example.finalassignment

import android.app.Application
import com.example.finalassignment.data.JournalRoomDatabase

class JournalApplication: Application() {
    val database: JournalRoomDatabase by lazy {JournalRoomDatabase.getDatabase(this)}
}