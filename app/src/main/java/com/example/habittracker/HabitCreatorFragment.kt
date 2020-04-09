package com.example.habittracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.habit_list_element.*
import kotlinx.android.synthetic.main.new_habit_fragment.*

class HabitCreatorFragment : Fragment() {

    companion object {
        fun newInstance(habit: Habit?): HabitCreatorFragment {
            val fragment = HabitCreatorFragment()
            val bundle = Bundle()

            if (habit != null) {
                bundle.putSerializable("Editable", habit)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

    private var priorityText: String = ""
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var viewModel: HabitEditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var editableHabit: Habit? = null
        arguments?.let {
            editableHabit = it.getSerializable("Editable") as Habit?
        }
        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitEditorViewModel(HabitsModel, editableHabit) as T
            }
        }).get(HabitEditorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.new_habit_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel.habit.observe(viewLifecycleOwner, Observer {
            habit ->
            deleteButton.isEnabled = false
                if (habit != null) {
                    fillEditingObject(habit)
                    deleteButton.isEnabled = true
                }
        })
    }

    private fun init() {
        adapter = ArrayAdapter(
            activity as Context,
            android.R.layout.simple_spinner_item,
            priorities
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prioritySpinner.adapter = adapter
        }

        prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                priorityText = ""
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                priorityText = parent?.getItemAtPosition(position).toString()
            }
        }

        createButton.setOnClickListener {
            val habit = getNewHabit()
            if (habit != null) {
                viewModel.putHabit(habit)
                (activity as MainActivity).openMainFragment()
            }
        }

        deleteButton.setOnClickListener {
            viewModel.deleteHabit()
            (activity as MainActivity).openMainFragment()
        }
    }

    private fun fillEditingObject(editableHabit: Habit) {
        nameEditText.setText(editableHabit.name)
        descriptionEditText.setText(editableHabit.description)
        prioritySpinner.setSelection(adapter.getPosition(prioritiesMap[editableHabit.priority]))
        when (editableHabit.type) {
            HabitType.Good -> goodRB.isChecked = true
            HabitType.Bad -> badRB.isChecked = true
        }
        repeatsEditText.setText(editableHabit.repeats.toString())
        periodEditText.setText(editableHabit.period.toString())
    }

    private fun getNewHabit(): Habit? {
        val name = nameEditText.text.toString()
        if (name.isEmpty())
            return null
        val description = descriptionEditText.text.toString()
        if (priorityText.isEmpty())
            return null
        lateinit var priority: Priority
        when (priorityText) {
            priorities[0] -> {
                priority = Priority.VeryLow
            }
            priorities[1] -> {
                priority = Priority.Low
            }
            priorities[2] -> {
                priority = Priority.Medium
            }
            priorities[3] -> {
                priority = Priority.High
            }
            priorities[4] -> {
                priority = Priority.Urgent
            }
        }
        lateinit var type: HabitType
        when {
            goodRB.isChecked -> {
                type = HabitType.Good
            }
            badRB.isChecked -> {
                type = HabitType.Bad
            }
        }
        val repeatsString = repeatsEditText.text.toString()
        if (repeatsString.isEmpty())
            return null
        val repeats = repeatsString.toInt()
        val periodString = periodEditText.text.toString()
        if (periodString.isEmpty())
            return null
        val period = periodString.toInt()
        return Habit(
            name, description, priority, type, repeats, period
        )
    }
}