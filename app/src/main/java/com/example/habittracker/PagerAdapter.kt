package com.example.habittracker

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(
    activity: AppCompatActivity,
    fragmentManager: FragmentManager
): FragmentPagerAdapter(fragmentManager) {

    val goodFragment: HabitViewFragment = HabitViewFragment.getNewInstance(activity, HabitType.Good)
    val badFragment: HabitViewFragment  = HabitViewFragment.getNewInstance(activity, HabitType.Bad)

    override fun getItem(position: Int): Fragment = when(position){
        0 -> goodFragment
        else -> badFragment
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = when(position){
        0 -> "Хорошие привычки"
        else -> "Плохие привычки"
    }

}