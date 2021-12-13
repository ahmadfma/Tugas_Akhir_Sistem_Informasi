package com.example.indonews_app.UserData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserData_Berita::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao():UserDao

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempinstance = INSTANCE
            if(tempinstance != null) {
                return tempinstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}