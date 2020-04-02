package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitEditorViewModel(private val model: HabitsModel, private val editableHabit: Habit?) : ViewModel() {

    private val mutableHabit: MutableLiveData<Habit?> = MutableLiveData()

    val habit: LiveData<Habit?> = mutableHabit

    init {
        load()
    }

    private fun load() {
        mutableHabit.postValue(editableHabit)
    }

    fun putHabit(habit: Habit){
        if (editableHabit != null) editHabit(habit) else addHabit(habit)
    }

    private fun addHabit(habit: Habit){
        model.addHabit(habit)
    }

    private fun editHabit(newHabit: Habit){
        model.editHabit(editableHabit!!, newHabit)
    }

}