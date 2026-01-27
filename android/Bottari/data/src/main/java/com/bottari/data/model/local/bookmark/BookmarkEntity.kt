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
    val items: List<String>,
    val hashtags: List<String>,
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun toBookmarkTemplate(): BookmarkTemplate =
        BookmarkTemplate(
            id = id,
            templateId = templateId,
            title = title,
            description = description,
            items = items,
            hashtags = hashtags,
            createdAt = createdAt,
        )

    companion object {
        fun fromBookmarkTemplate(src: BookmarkTemplate): BookmarkEntity =
            BookmarkEntity(
                id = src.id,
                templateId = src.templateId,
                title = src.title,
                description = src.description,
                items = src.items,
                hashtags = src.hashtags,
                createdAt = src.createdAt ?: System.currentTimeMillis(),
            )
    }
}
