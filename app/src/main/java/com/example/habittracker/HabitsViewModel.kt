package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class HabitsViewModel(private val model: HabitsModel) : ViewModel() {

    private val mutableGoodHabits: MutableLiveData<List<Habit>?> = MutableLiveData()
    private val mutableBadHabits: MutableLiveData<List<Habit>?> = MutableLiveData()

    val goodHabits: LiveData<List<Habit>?> = mutableGoodHabits
    val badHabits: LiveData<List<Habit>?> = mutableBadHabits

    init {
        load()
    }

    private fun load() {
        val habits = model.getHabits()

        val good = habits.filter {
            it.type == HabitType.Good
        }
        mutableGoodHabits.postValue(good)

        val bad = habits.filter {
            it.type == HabitType.Bad
        }
        mutableBadHabits.postValue(bad)
    }

    fun filter(filter: String, sortByPriority: Boolean) {
        val habits = model.getHabits()

        var good = habits.filter {
            it.type == HabitType.Good && it.name
                .toLowerCase(Locale.getDefault())
                .contains(
                    filter.toLowerCase(Locale.getDefault())
                )
        }
        if (sortByPriority) good = good.sorted()
        mutableGoodHabits.postValue(good)

        var bad = habits.filter {
            it.type == HabitType.Bad && it.name
                .toLowerCase(Locale.getDefault())
                .contains(filter.toLowerCase(Locale.getDefault())
            )
        }
        if (sortByPriority) bad = bad.sorted()
        mutableBadHabits.postValue(bad)
    }

}