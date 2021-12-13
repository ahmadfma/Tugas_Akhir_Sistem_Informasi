package com.example.indonews_app.UserData

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "UserViewModel"

class UserViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<UserData_Berita>>
    private val repository: UserRepository

    init {
        Log.d(TAG, "Created")
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addArticle(userDataBerita: UserData_Berita) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addArticle(userDataBerita)
            Log.d(TAG, "addArticle")
        }
    }

    fun deleteArticle(userDataBerita: UserData_Berita) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArticle(userDataBerita)
            Log.d(TAG, "deleteArticle")
        }
    }

    fun deleteAllArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllArticle()
            Log.d(TAG, "deleteAllArticle")
        }
    }

    fun deleteSpecificArticleFromDatabase(key1: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSpecificArticleFromDatabase(key1)
            Log.d(TAG, "deleteSpecificArticleFromDatabase")
        }
    }

}