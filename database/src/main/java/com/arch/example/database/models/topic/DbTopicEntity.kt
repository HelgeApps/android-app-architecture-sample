package com.arch.example.database.models.topic

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arch.example.database.models.photo.DbCoverPhoto
import com.arch.example.database.models.photo.asEntity
import com.arch.example.database.models.photo.asExternalModel
import com.arch.example.entities.topic.Topic
import java.time.Instant

@Entity(tableName = "topics")
data class DbTopicEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "published_at")
    val publishedAt: Instant,

    @Embedded(prefix = "cover_photo")
    val coverPhoto: DbCoverPhoto?
)

fun DbTopicEntity.asExternalModel() = Topic(
    id = this.id,
    title = this.title,
    description = this.description,
    publishedAt = this.publishedAt,
    coverPhoto = this.coverPhoto?.asExternalModel()
)

fun Topic.asEntity() = DbTopicEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    publishedAt = this.publishedAt,
    coverPhoto = this.coverPhoto?.asEntity()
)
