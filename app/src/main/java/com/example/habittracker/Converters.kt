package com.example.habittracker

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.toString()

    @TypeConverter
    fun toPriority(data: String): Priority = Priority.valueOf(data)

    @TypeConverter
    fun fromType(type: HabitType): String = type.toString()

    @TypeConverter
    fun toType(data: String): HabitType = HabitType.valueOf(data)

}