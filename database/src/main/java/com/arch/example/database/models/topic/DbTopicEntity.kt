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

@Entity(tableName = DbTopicEntity.TABLE_NAME)
data class DbTopicEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,

    @ColumnInfo(name = COLUMN_TITLE)
    val title: String,

    @ColumnInfo(name = COLUMN_DESCRIPTION)
    val description: String?,

    @ColumnInfo(name = COLUMN_PUBLISHED_AT)
    val publishedAt: Instant,

    @Embedded(prefix = COLUMN_COVER_PHOTO)
    val coverPhoto: DbCoverPhoto?
) {
    companion object {
        const val TABLE_NAME = "topics"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PUBLISHED_AT = "published_at"
        const val COLUMN_COVER_PHOTO = "cover_photo"
    }
}


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