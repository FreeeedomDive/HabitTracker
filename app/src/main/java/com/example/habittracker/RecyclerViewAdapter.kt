package com.example.habittracker

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private var values: List<Habit>,
    private val listener: HabitsClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, private val listener: HabitsClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
        val repeatsTextView: TextView = itemView.findViewById(R.id.repeatsTextView)
        val periodTextView: TextView = itemView.findViewById(R.id.periodTextView)
        val habitColor: TextView = itemView.findViewById(R.id.habitColor)

        override fun onClick(v: View?) {
            val clicked = adapterPosition
            listener.onListItemClick(clicked)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    fun getElement(id: Int): Habit = values[id]

    fun setItems(list: List<Habit>){
        values = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.habit_list_element, parent, false)
        return ViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = values.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = values[position]
        holder.nameTextView.text = habit.name
        holder.descriptionTextView.text = when {
            habit.description.isEmpty() -> context.getString(R.string.emptyDescription)
            else -> habit.description
        }
        holder.priorityTextView.text =
            "${context.getString(R.string.priorityTemplate)} ${prioritiesMap[habit.priority]}"
        when (habit.type) {
            HabitType.Bad -> holder.habitColor.setBackgroundColor(badHabitColor)
            HabitType.Good -> holder.habitColor.setBackgroundColor(goodHabitColor)
        }
        holder.repeatsTextView.text =
            "${context.getString(R.string.repeatsTemplate)} ${habit.repeats}"
        holder.periodTextView.text =
            "${context.getString(R.string.periodTemplate)} ${habit.period}"
    }
}