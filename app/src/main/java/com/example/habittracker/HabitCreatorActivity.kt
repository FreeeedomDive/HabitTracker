package com.example.habittracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class HabitCreatorFragment : Fragment() {

    companion object {
        fun getNewInstance(editedHabit: Habit?): HabitCreatorFragment {
            val fragment = HabitCreatorFragment()
            val bundle = Bundle()

            if (editedHabit != null) {
                bundle.putSerializable("Editable", editedHabit)
            }

            fragment.arguments = bundle
            return fragment
        }
    }

    private var editableHabit: Habit? = null

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var goodRB: RadioButton
    private lateinit var badRB: RadioButton
    private lateinit var repeatsEditText: EditText
    private lateinit var periodEditText: EditText
    private lateinit var createButton: Button

    private var priorityText: String = ""
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.new_habit_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            editableHabit = it.getSerializable("Editable") as Habit?

        }

        init()
        if (editableHabit != null) {
            fillEditingObject()
        }
    }

    private fun init() {
        nameEditText = view!!.findViewById(R.id.nameEditText)
        descriptionEditText = view!!.findViewById(R.id.descriptionEditText)
        prioritySpinner = view!!.findViewById(R.id.prioritySpinner)
        goodRB = view!!.findViewById(R.id.goodRB)
        badRB = view!!.findViewById(R.id.badRB)
        repeatsEditText = view!!.findViewById(R.id.repeatsEditText)
        periodEditText = view!!.findViewById(R.id.periodEditText)
        createButton = view!!.findViewById(R.id.createButton)

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
                val intent = Intent().apply {
                    putExtra("NEW_HABIT", habit)
                }
                val code = when (editableHabit) {
                    null -> CREATE_CODE
                    else -> EDIT_CODE
                }
                activity!!.setResult(code, intent)
                activity!!.finish()
            }
        }
    }

    private fun fillEditingObject() {
        nameEditText.setText(editableHabit!!.name)
        descriptionEditText.setText(editableHabit!!.description)
        prioritySpinner.setSelection(adapter.getPosition(prioritiesMap[editableHabit!!.priority]))
        when (editableHabit!!.type) {
            HabitType.Good -> goodRB.isChecked = true
            HabitType.Bad -> badRB.isChecked = true
        }
        repeatsEditText.setText(editableHabit!!.repeats.toString())
        periodEditText.setText(editableHabit!!.period.toString())
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
            fragment = HabitCreatorFragment.getNewInstance(habit)
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
