package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: Long? = null,
    val title: String = "",
    val description: String = "",
    val photoUri: String,
    val createdAt: Long = System.currentTimeMillis(),
    val location: String = "",
    val tags: String = "", // Comma-separated or categories
    val people: String = "",
    val isFavorite: Boolean = false
)
