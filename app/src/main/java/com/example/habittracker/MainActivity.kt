package com.example.habittracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import kotlinx.android.synthetic.main.activity_main.*

interface HabitsClickListener {
    fun onListItemClick(clickedItem: Int)
}

class HabitViewFragment : Fragment(), HabitsClickListener {

    companion object {
        fun getNewInstance(context: Context, type: HabitType): HabitViewFragment {
            val fragment = HabitViewFragment()
            val bundle = Bundle()
            when (type) {
                HabitType.Bad -> bundle.putString("type", context.getString(R.string.badType))
                HabitType.Good -> bundle.putString("type", context.getString(R.string.goodType))
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var type: HabitType
    private var lastClicked = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.habit_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerViewAdapter = RecyclerViewAdapter(mutableListOf(), this, view.context)
        recyclerView.adapter = recyclerViewAdapter

        arguments?.let {
            type = when (it.getString("type", "")) {
                "good" -> HabitType.Good
                else -> HabitType.Bad
            }
        }
    }

    override fun onListItemClick(clickedItem: Int) {
        lastClicked = clickedItem
        val clicked = recyclerViewAdapter.getElement(clickedItem)
        val intent = Intent(activity, HabitCreatorActivity::class.java).apply {
            this.putExtra(getString(R.string.mode), getString(R.string.edit))
            this.putExtra(getString(R.string.editableObjectCode), clicked)
        }
        startActivityForResult(intent, EDIT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == EDIT_CODE && data != null) {

            val lastHabit = recyclerViewAdapter.getElement(lastClicked)

            val editedHabit: Habit =
                data.extras?.getSerializable(getString(R.string.newHabitCode)) as Habit

            if (lastHabit.type == editedHabit.type) {
                recyclerViewAdapter.editElement(editedHabit, lastClicked)
            } else {
                when (type) {
                    HabitType.Good -> {
                        recyclerViewAdapter.removeElement(lastHabit)
                        (activity as MainActivity).badFragment.recyclerViewAdapter.addElement(
                            editedHabit
                        )
                    }
                    HabitType.Bad -> {
                        recyclerViewAdapter.removeElement(lastHabit)
                        (activity as MainActivity).goodFragment.recyclerViewAdapter.addElement(
                            editedHabit
                        )
                    }
                }
            }
        }
        lastClicked = -1
    }
}

class RecyclerViewAdapter(
    private val values: MutableList<Habit>,
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

    fun addElement(habit: Habit) {
        values.add(habit)
        notifyItemInserted(values.indexOf(habit))
    }

    fun removeElement(habit: Habit) {
        val index = values.indexOf(habit)
        values.remove(habit)
        notifyItemRemoved(index)
    }

    fun getElement(id: Int): Habit = values[id]

    fun editElement(new: Habit, position: Int) {
        val edited = values[position]
        edited.name = new.name
        edited.description = new.description
        edited.priority = new.priority
        edited.type = new.type
        edited.repeats = new.repeats
        edited.period = new.period
        notifyItemChanged(position)
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

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    lateinit var goodFragment: HabitViewFragment
    lateinit var badFragment: HabitViewFragment
    private lateinit var navigationValues: Array<String>
    private lateinit var navigationDrawer: ListView
    private lateinit var drawer: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        title = getString(R.string.mainActivityTitle)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val adapter = ViewPagerAdapter(this, supportFragmentManager)
        goodFragment = adapter.goodFragment
        badFragment = adapter.badFragment
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        fab.setOnClickListener {
            val intent = Intent(this, HabitCreatorActivity::class.java).apply {
                this.putExtra(getString(R.string.mode), getString(R.string.create))
            }
            startActivityForResult(intent, CREATE_CODE)
        }

        drawer = findViewById(R.id.drawer)

        navigationValues = resources.getStringArray(R.array.nav_values)
        navigationDrawer = findViewById(R.id.left_drawer)
        navigationDrawer.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navigationValues)
        navigationDrawer.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when(position){
                    1 -> startActivity(Intent(this, AboutActivity::class.java))
                }
                drawer.closeDrawers()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE && data != null) {
            val newHabit: Habit =
                data.extras?.getSerializable(getString(R.string.newHabitCode)) as Habit
            when (newHabit.type) {
                HabitType.Good -> goodFragment.recyclerViewAdapter.addElement(newHabit)
                HabitType.Bad -> badFragment.recyclerViewAdapter.addElement(newHabit)
            }
        }
    }
}
