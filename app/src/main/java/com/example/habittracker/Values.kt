package com.example.habittracker

import android.graphics.Color

const val CREATE_CODE = 228
const val EDIT_CODE = 322

val priorities = listOf(
    "Очень низкий",
    "Низкий",
    "Средний",
    "Высокий",
    "Важный"
)

val prioritiesMap = mapOf(
    Priority.VeryLow to priorities[0],
    Priority.Low to priorities[1],
    Priority.Medium to priorities[2],
    Priority.High to priorities[3],
    Priority.Urgent to priorities[4]
)

val goodHabitColor = Color.parseColor("#00aa00")
val badHabitColor = Color.parseColor("#aa0000")
