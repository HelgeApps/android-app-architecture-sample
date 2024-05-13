package com.arch.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.arch.example.database.models.topic.DbTopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    // TODO: don't use REPLACE strategy if you have relations with the data, use Insert and Update in transaction method to save topics
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topics: List<DbTopicEntity>): List<Long>

    @Query("SELECT * FROM ${DbTopicEntity.TABLE_NAME}")
    fun getAll(): Flow<List<DbTopicEntity>>

    @Query("SELECT * FROM ${DbTopicEntity.TABLE_NAME} WHERE ${DbTopicEntity.COLUMN_ID}=:id")
    fun getTopicById(id: String): Flow<DbTopicEntity>

    @Query("DELETE FROM ${DbTopicEntity.TABLE_NAME}")
    suspend fun deleteAll(): Int // or return nothing if no need to know count of deleted rows

    @Transaction
    suspend fun deleteAllAndAddNewTopics(topics: List<DbTopicEntity>) {
        // Anything inside this method runs in a single transaction.
        deleteAll()
        insertAll(topics)
    }
}