package com.example.data.repository

import com.example.data.local.CategoryDao
import com.example.data.local.EventDao
import com.example.data.local.MemoryDao
import com.example.data.model.CategoryEntity
import com.example.data.model.EventEntity
import com.example.data.model.MemoryEntity
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class MinticeRepository(
    private val eventDao: EventDao,
    private val memoryDao: MemoryDao,
    private val categoryDao: CategoryDao
) {
    fun getAllEvents(): Flow<List<EventEntity>> = eventDao.getAllEvents()

    fun getEventById(id: Long): Flow<EventEntity?> = eventDao.getEventById(id)

    suspend fun getEventByIdSync(id: Long): EventEntity? = eventDao.getEventByIdSync(id)

    fun getUpcomingEvents(fromTime: Long): Flow<List<EventEntity>> = eventDao.getUpcomingEvents(fromTime)

    fun getEventsInRange(startTime: Long, endTime: Long): Flow<List<EventEntity>> =
        eventDao.getEventsInRange(startTime, endTime)

    suspend fun getPendingReminderEvents(currentTime: Long): List<EventEntity> =
        eventDao.getPendingReminderEvents(currentTime)

    fun searchEvents(query: String): Flow<List<EventEntity>> = eventDao.searchEvents(query)

    suspend fun insertEvent(event: EventEntity): Long = eventDao.insertEvent(event)

    suspend fun updateEvent(event: EventEntity) = eventDao.updateEvent(event)

    suspend fun deleteEvent(event: EventEntity) = eventDao.deleteEvent(event)

    suspend fun deleteEventById(id: Long) = eventDao.deleteEventById(id)

    // Memories
    fun getAllMemories(): Flow<List<MemoryEntity>> = memoryDao.getAllMemories()

    fun getMemoriesForEvent(eventId: Long): Flow<List<MemoryEntity>> = memoryDao.getMemoriesForEvent(eventId)

    fun getFavoriteMemories(): Flow<List<MemoryEntity>> = memoryDao.getFavoriteMemories()

    fun searchMemories(query: String): Flow<List<MemoryEntity>> = memoryDao.searchMemories(query)

    suspend fun insertMemory(memory: MemoryEntity): Long = memoryDao.insertMemory(memory)

    suspend fun updateMemory(memory: MemoryEntity) = memoryDao.updateMemory(memory)

    suspend fun deleteMemory(memory: MemoryEntity) = memoryDao.deleteMemory(memory)

    suspend fun deleteMemoryById(id: Long) = memoryDao.deleteMemoryById(id)

    // Categories
    fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: CategoryEntity): Long = categoryDao.insertCategory(category)

    suspend fun deleteCategory(category: CategoryEntity) = categoryDao.deleteCategory(category)

    suspend fun ensureDefaultCategories() {
        if (categoryDao.getCategoryCount() == 0) {
            val defaults = listOf(
                CategoryEntity(name = "Birthday", iconName = "cake", colorHex = "#D96B82"),
                CategoryEntity(name = "Festival", iconName = "festival", colorHex = "#E28D38"),
                CategoryEntity(name = "Anniversary", iconName = "heart", colorHex = "#E05A74"),
                CategoryEntity(name = "Personal", iconName = "sparkle", colorHex = "#9B68A8"),
                CategoryEntity(name = "Health", iconName = "fitness", colorHex = "#4E9B68"),
                CategoryEntity(name = "Work", iconName = "work", colorHex = "#4A89C8"),
                CategoryEntity(name = "Travel", iconName = "flight", colorHex = "#369FA3"),
                CategoryEntity(name = "Reminder", iconName = "bell", colorHex = "#D49A32")
            )
            categoryDao.insertAll(defaults)
        }
    }

    // Backup & Export JSON
    suspend fun exportToJson(events: List<EventEntity>, memories: List<MemoryEntity>): String {
        val root = JSONObject()
        root.put("version", 1)
        root.put("appName", "Mintice")
        root.put("exportedAt", System.currentTimeMillis())

        val eventsArray = JSONArray()
        for (e in events) {
            val obj = JSONObject()
            obj.put("id", e.id)
            obj.put("title", e.title)
            obj.put("description", e.description)
            obj.put("eventType", e.eventType)
            obj.put("category", e.category)
            obj.put("startDateTime", e.startDateTime)
            obj.put("endDateTime", e.endDateTime)
            obj.put("isAllDay", e.isAllDay)
            obj.put("locationName", e.locationName)
            obj.put("colorHex", e.colorHex)
            obj.put("iconName", e.iconName)
            obj.put("coverImageUri", e.coverImageUri ?: "")
            obj.put("reminderEnabled", e.reminderEnabled)
            obj.put("reminderMinutesBefore", e.reminderMinutesBefore)
            obj.put("recurrenceRule", e.recurrenceRule)
            obj.put("isCompleted", e.isCompleted)
            obj.put("birthYear", e.birthYear ?: -1)
            eventsArray.put(obj)
        }
        root.put("events", eventsArray)

        val memoriesArray = JSONArray()
        for (m in memories) {
            val obj = JSONObject()
            obj.put("id", m.id)
            obj.put("eventId", m.eventId ?: -1L)
            obj.put("title", m.title)
            obj.put("description", m.description)
            obj.put("photoUri", m.photoUri)
            obj.put("createdAt", m.createdAt)
            obj.put("location", m.location)
            obj.put("tags", m.tags)
            obj.put("people", m.people)
            obj.put("isFavorite", m.isFavorite)
            memoriesArray.put(obj)
        }
        root.put("memories", memoriesArray)

        return root.toString(2)
    }

    suspend fun importFromJson(jsonString: String): Boolean {
        return try {
            val root = JSONObject(jsonString)
            val eventsArray = root.optJSONArray("events") ?: JSONArray()
            val memoriesArray = root.optJSONArray("memories") ?: JSONArray()

            val importedEvents = mutableListOf<EventEntity>()
            for (i in 0 until eventsArray.length()) {
                val obj = eventsArray.getJSONObject(i)
                val birthYearVal = obj.optInt("birthYear", -1)
                importedEvents.add(
                    EventEntity(
                        title = obj.getString("title"),
                        description = obj.optString("description", ""),
                        eventType = obj.optString("eventType", "Personal"),
                        category = obj.optString("category", "Personal"),
                        startDateTime = obj.getLong("startDateTime"),
                        endDateTime = obj.getLong("endDateTime"),
                        isAllDay = obj.optBoolean("isAllDay", false),
                        locationName = obj.optString("locationName", ""),
                        colorHex = obj.optString("colorHex", "#D96B82"),
                        iconName = obj.optString("iconName", "cake"),
                        coverImageUri = obj.optString("coverImageUri").takeIf { it.isNotBlank() },
                        reminderEnabled = obj.optBoolean("reminderEnabled", false),
                        reminderMinutesBefore = obj.optInt("reminderMinutesBefore", 60),
                        recurrenceRule = obj.optString("recurrenceRule", "NONE"),
                        isCompleted = obj.optBoolean("isCompleted", false),
                        birthYear = if (birthYearVal > 0) birthYearVal else null
                    )
                )
            }

            val importedMemories = mutableListOf<MemoryEntity>()
            for (i in 0 until memoriesArray.length()) {
                val obj = memoriesArray.getJSONObject(i)
                val eventIdVal = obj.optLong("eventId", -1L)
                importedMemories.add(
                    MemoryEntity(
                        eventId = if (eventIdVal > 0) eventIdVal else null,
                        title = obj.optString("title", ""),
                        description = obj.optString("description", ""),
                        photoUri = obj.getString("photoUri"),
                        createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                        location = obj.optString("location", ""),
                        tags = obj.optString("tags", ""),
                        people = obj.optString("people", ""),
                        isFavorite = obj.optBoolean("isFavorite", false)
                    )
                )
            }

            if (importedEvents.isNotEmpty()) {
                eventDao.insertAll(importedEvents)
            }
            if (importedMemories.isNotEmpty()) {
                memoryDao.insertAll(importedMemories)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
