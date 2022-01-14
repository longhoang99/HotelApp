package com.example.hotelapp.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.hotelapp.R
import com.example.hotelapp.ui.adapter.RoomViewPagerAdapter
import com.example.hotelapp.ui.service.RoomServiceFragment
import com.example.hotelapp.ui.service.ServiceListFragment
import com.google.android.material.tabs.TabLayout

class StatisticFragment : Fragment() {
    private lateinit var tabRoom: TabLayout
    private lateinit var viewPagerRoom: ViewPager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabRoom = requireView().findViewById(R.id.tabRoom)
        viewPagerRoom = requireView().findViewById(R.id.viewPagerRoom)
        setupViews()
    }
    private fun setupViews() {
        setAdapter()
        setTabsWithViewPager()
    }

    private fun setAdapter() {
        RoomViewPagerAdapter(childFragmentManager).apply {
            addFragment(GeneralFragment(), getString(R.string.statistic_general))
            addFragment(UserFragment(), getString(R.string.statistic_user))
            viewPagerRoom.adapter = this
        }
    }

    private fun setTabsWithViewPager() {
        tabRoom.apply {
            addTab(tabRoom.newTab())
            addTab(tabRoom.newTab())
            tabGravity = TabLayout.GRAVITY_FILL
            setupWithViewPager(viewPagerRoom)
        }
    }
}