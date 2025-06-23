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
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getTopicsDao(): TopicDao

    companion object {
        private const val DB_FILE_NAME = "database"

        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDataBase::class.java, DB_FILE_NAME)
                .build()
    }
}
