package com.example.habittracker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    private lateinit var viewModel: HabitsViewModel
    private lateinit var goodFragment: HabitViewFragment
    private lateinit var badFragment: HabitViewFragment
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitsViewModel(HabitsModel) as T
            }
        }).get(HabitsViewModel::class.java)
        adapter = ViewPagerAdapter(childFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        goodFragment = adapter.goodFragment
        badFragment = adapter.badFragment

        setBottomSheetBehavior()

        fab.setOnClickListener {
            (activity as MainActivity).openEditor(null)
        }

        viewModel.goodHabits.observe(viewLifecycleOwner, Observer {
            if (it != null) goodFragment.putItems(it)
        })
        viewModel.badHabits.observe(viewLifecycleOwner, Observer {
            if (it != null) badFragment.putItems(it)
        })
    }

    private fun setBottomSheetBehavior() {
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()
            }
        })

        nameFilter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filter(s.toString(), prioritySortCheckBox.isChecked)
            }
        })

        prioritySortCheckBox.setOnClickListener {
            viewModel.filter(nameFilter.text.toString(), prioritySortCheckBox.isChecked)
        }
    }

}