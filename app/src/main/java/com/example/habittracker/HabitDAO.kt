package com.example.habittracker

import androidx.room.*

@Dao
interface HabitDAO {

    @Insert
    fun insertAll(vararg habits: Habit)

    @Delete
    fun delete(habit: Habit)

    @Update
    fun update(habit: Habit)

    @Query("SELECT * FROM habit")
    fun getAll(): List<Habit>

}