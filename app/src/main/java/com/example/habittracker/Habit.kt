package com.example.habittracker

import java.io.Serializable

enum class HabitType {
    Good,
    Bad
}

enum class Priority {
    VeryLow,
    Low,
    Medium,
    High,
    Urgent
}

class Habit(
    var name: String,
    var description: String,
    var priority: Priority,
    var type: HabitType,
    var repeats: Int,
    var period: Int
) : Serializable
