package com.example.habittracker

object HabitsModel {

    private val habits: MutableList<Habit> = mutableListOf()

    fun getHabits(): List<Habit> {
        return habits
    }

    fun addHabit(habit: Habit) {
        habits.add(habit)
    }

    fun editHabit(oldHabit: Habit, newHabit: Habit) {
        val editable = habits[habits.indexOf(oldHabit)]
        editable.name = newHabit.name
        editable.description = newHabit.description
        editable.priority = newHabit.priority
        editable.type = newHabit.type
        editable.repeats = newHabit.repeats
        editable.period = newHabit.period
    }
}