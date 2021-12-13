package com.example.indonews_app.UserData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addArticle(userDataBerita: UserData_Berita)

    @Query("SELECT * FROM user_table")
    fun readAllData(): LiveData<List<UserData_Berita>>

    @Delete
    fun deleteArticle(userDataBerita: UserData_Berita)

    @Query("DELETE FROM user_table")
    fun deleteAllArticle()

    @Query("DELETE FROM user_table WHERE berita_id = :key1")
    fun deleteSpecificArticle(key1: String)
}