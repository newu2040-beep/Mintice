package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CategoryEntity
import com.example.data.model.EventEntity
import com.example.data.model.MemoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [EventEntity::class, MemoryEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MinticeDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun memoryDao(): MemoryDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: MinticeDatabase? = null

        fun getInstance(context: Context): MinticeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MinticeDatabase::class.java,
                    "mintice_database.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                populateInitialCategories(getInstance(context).categoryDao())
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialCategories(dao: CategoryDao) {
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
            dao.insertAll(defaults)
        }
    }
}
