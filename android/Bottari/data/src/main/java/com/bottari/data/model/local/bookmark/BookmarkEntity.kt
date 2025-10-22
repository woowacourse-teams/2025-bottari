package com.bottari.data.model.local.bookmark

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bottari.domain.model.bottari.template.BookmarkTemplate

@Entity(
    tableName = "bookmark",
    indices = [Index(value = ["templateId"], unique = true)],
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long,
    val title: String,
    val description: String,
    val items: String,
    val hashtags: String,
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun toBookmarkTemplate(): BookmarkTemplate =
        BookmarkTemplate(
            id = id,
            templateId = templateId,
            title = title,
            description = description,
            items = items.split(","),
            hashtags = hashtags.split(","),
            createdAt = createdAt,
        )

    companion object {
        fun fromBookmarkTemplate(bookmarkTemplate: BookmarkTemplate): BookmarkEntity =
            BookmarkEntity(
                id = bookmarkTemplate.id,
                templateId = bookmarkTemplate.templateId,
                title = bookmarkTemplate.title,
                description = bookmarkTemplate.description,
                items = bookmarkTemplate.items.joinToString(","),
                hashtags = bookmarkTemplate.hashtags.joinToString(","),
                createdAt = bookmarkTemplate.createdAt ?: System.currentTimeMillis(),
            )
    }
}
