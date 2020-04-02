package com.example.habittracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    var goodFragment: HabitViewFragment = HabitViewFragment.newInstance()
    var badFragment: HabitViewFragment = HabitViewFragment.newInstance()

    override fun getItem(position: Int): Fragment = when (position) {
        0 ->
            goodFragment
        else ->
            badFragment
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "Хорошие привычки"
        else -> "Плохие привычки"
    }

}