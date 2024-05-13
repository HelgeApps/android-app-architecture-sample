package com.arch.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arch.example.database.dao.TopicDao
import com.arch.example.database.models.topic.DbTopicEntity
import com.arch.example.database.utils.Converters

@Database(
    entities = [DbTopicEntity::class],
    version = 6,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getTopicsDao(): TopicDao

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDataBase::class.java, NAME)
                .fallbackToDestructiveMigration()
                .build()

        private const val NAME = "database"
    }
}