package com.chareem.master.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chareem.core.Constant
import com.chareem.master.data.model.*

@Database(entities = [MovieGenre::class],
    version = Constant.DATABASE_VERSION, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {

    abstract fun movieGenreDAO(): MovieGenreDAO

    companion object {
        var database: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (database == null) {
                synchronized(AppDatabase::class.java) {
                    if (database == null) {
                        database = buildDatabase(context)
                    }
                }
            }
            return database
        }

        fun buildDatabase(applicationContext: Context): AppDatabase? {
            return Room.databaseBuilder(applicationContext.applicationContext, AppDatabase::class.java, Constant.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                //.allowMainThreadQueries()
                .build()
        }
    }
}