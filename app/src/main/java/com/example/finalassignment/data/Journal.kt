package com.example.finalassignment.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal")
data class Journal (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "location")
    val location: String = "",
    @ColumnInfo(name = "date")
    val date: String = "",
    @ColumnInfo(name = "entry")
    val entry: String = ""
)