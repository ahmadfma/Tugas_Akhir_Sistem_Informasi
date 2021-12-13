package com.example.indonews_app.UserData

import android.util.Log
import androidx.lifecycle.LiveData

const val tag = "UserRepository"

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<UserData_Berita>> = userDao.readAllData()

    fun addArticle(userDataBerita: UserData_Berita) {
        userDao.addArticle(userDataBerita)
        Log.d(tag, "addArticle")
    }

    fun deleteArticle(userDataBerita: UserData_Berita) {
        userDao.deleteArticle(userDataBerita)
        Log.d(tag, "deleteArticle")
    }

    fun deleteAllArticle() {
        userDao.deleteAllArticle()
        Log.d(tag, "deleteAllArticle")
    }

    fun deleteSpecificArticleFromDatabase(key1: String) {
        userDao.deleteSpecificArticle(key1)
        Log.d(tag, "deleteSpecificArticleFromDatabase")
    }

}