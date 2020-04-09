package com.example.habittracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Habit::class], version = 1)
abstract class DatabaseContext : RoomDatabase() {
    abstract fun habitDao(): HabitDAO

    companion object {
        @Volatile
        private var instance: DatabaseContext? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            DatabaseContext::class.java, "habits.db"
        ).allowMainThreadQueries().build()
    }

}