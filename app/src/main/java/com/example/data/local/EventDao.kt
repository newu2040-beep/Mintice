package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY startDateTime ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    fun getEventById(id: Long): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getEventByIdSync(id: Long): EventEntity?

    @Query("SELECT * FROM events WHERE startDateTime >= :minTime ORDER BY startDateTime ASC")
    fun getUpcomingEvents(minTime: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE (startDateTime BETWEEN :startTime AND :endTime) OR (endDateTime BETWEEN :startTime AND :endTime) ORDER BY startDateTime ASC")
    fun getEventsInRange(startTime: Long, endTime: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE reminderEnabled = 1 AND isCompleted = 0 AND startDateTime > :currentTime")
    suspend fun getPendingReminderEvents(currentTime: Long): List<EventEntity>

    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR locationName LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchEvents(query: String): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Long)

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
}
