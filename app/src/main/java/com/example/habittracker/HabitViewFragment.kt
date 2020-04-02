package com.example.habittracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.habit_fragment.*

class HabitViewFragment : Fragment(), HabitsClickListener {

    companion object {
        fun newInstance(): HabitViewFragment {
            return HabitViewFragment()
        }
    }

    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.habit_fragment, container, false)

    private var values = listOf<Habit>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerViewAdapter = RecyclerViewAdapter(mutableListOf(), this, view.context)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter?.setItems(values)
    }

    override fun onListItemClick(clickedItem: Int) {
        val clicked = recyclerViewAdapter?.getElement(clickedItem)
        (activity as MainActivity).openEditor(clicked)
    }

    fun putItems(list: List<Habit>){
        values = list
        recyclerViewAdapter?.setItems(values)
    }
}