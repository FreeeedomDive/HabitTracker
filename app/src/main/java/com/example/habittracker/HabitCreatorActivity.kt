package com.example.habittracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

import kotlinx.android.synthetic.main.new_habit_fragment.*

class HabitCreatorActivity : AppCompatActivity() {
    private var mode = "Create"
    private lateinit var fragment: HabitCreatorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_creator)

        var habit: Habit? = null
        mode = intent.extras?.getString("Mode")!!
        if (mode == "Edit") {
            title = getString(R.string.editHabit)
            habit = intent.extras?.getSerializable("Editable") as Habit
        } else if (mode == "Create") {
            title = getString(R.string.createNewHabit)
        }

        if (savedInstanceState == null) {
            fragment = HabitCreatorFragment.newInstance(habit)
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.new_habit_fragment,
                    fragment
                )
                .commit()
        }
    }
}
