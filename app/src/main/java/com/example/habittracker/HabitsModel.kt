package com.example.habittracker

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object HabitsModel {

    lateinit var appContext: Context
    lateinit var db: DatabaseContext

    fun putContext(context: Context){
        appContext = context
        db = DatabaseContext(appContext)
    }

    fun getHabits(): List<Habit> {
        return db.habitDao().getAll()
    }

    fun addHabit(habit: Habit) {
        GlobalScope.launch {
            db.habitDao().insertAll(habit)
        }
    }

    fun editHabit(newHabit: Habit) {
        GlobalScope.launch {
            db.habitDao().update(newHabit)
        }
    }

    fun deleteHabit(habit: Habit){
        GlobalScope.launch {
            db.habitDao().delete(habit)
        }
    }
}