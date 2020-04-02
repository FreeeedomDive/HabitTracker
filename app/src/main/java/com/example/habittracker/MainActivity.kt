package com.example.habittracker

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mainFragmentOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openMainFragment()

        val navigationValues = resources.getStringArray(R.array.nav_values)
        leftDrawer.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navigationValues)
        leftDrawer.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {
                        openMainFragment()
                    }
                    1 -> {
                        startActivity(Intent(this, AboutActivity::class.java))
                    }
                }
                drawer.closeDrawers()
            }
    }

    fun openMainFragment() {
        mainFragmentOpened = true
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MainFragment.newInstance())
            .commit()
        title = getString(R.string.mainActivityTitle)
    }

    fun openEditor(habit: Habit?) {
        mainFragmentOpened = false
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HabitCreatorFragment.newInstance(habit))
            .commit()
        title = if (habit != null)
            getString(R.string.editHabit)
        else
            getString(R.string.createNewHabit)
    }

    override fun onBackPressed() {
        if (!mainFragmentOpened) openMainFragment()
        else super.onBackPressed()
    }
}
