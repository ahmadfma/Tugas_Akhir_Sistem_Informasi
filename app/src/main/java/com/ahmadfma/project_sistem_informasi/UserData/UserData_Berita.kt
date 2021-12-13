package com.example.indonews_app.UserData

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.indonews_app.Model.Source

@Entity(tableName = "user_table")
data class UserData_Berita (
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        var berita_id: String,
    )