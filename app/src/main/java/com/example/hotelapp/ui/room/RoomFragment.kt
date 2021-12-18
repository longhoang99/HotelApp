package com.example.yummy.ui.favorite

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.hotelapp.R
import com.example.hotelapp.ui.adapter.RoomViewPagerAdapter
import com.example.hotelapp.ui.room.AvailableRoomFragment
import com.example.hotelapp.ui.room.BookedRoomFragment
import com.google.android.material.tabs.TabLayout

class RoomFragment: Fragment() {

    private lateinit var tabRoom: TabLayout
    private lateinit var viewPagerRoom: ViewPager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room, container, false)
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
            addFragment(AvailableRoomFragment(), getString(R.string.available_room))
            addFragment(BookedRoomFragment(), getString(R.string.booked_room))
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
