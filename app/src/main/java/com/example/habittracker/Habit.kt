package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

enum class HabitType{
    Good,
    Bad
}

enum class Priority(val priority: Int) {
    VeryLow(1),
    Low(2),
    Medium(3),
    High(4),
    Urgent(5)
}

@Entity
@TypeConverters(Converters::class)
data class Habit(
    @PrimaryKey var name: String,
    var description: String,
    var priority: Priority,
    var type: HabitType,
    var repeats: Int,
    var period: Int
) : Serializable, Comparable<Habit> {

    override fun compareTo(other: Habit): Int {
        return -this.priority.compareTo(other.priority)
    }
}
