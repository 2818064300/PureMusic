package com.lie.puremusic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentViewPagerAdapter(
    frgManager: FragmentManager,
    fragments: MutableList<Fragment>,
    lifecycle: Lifecycle,
) :
    FragmentStateAdapter(frgManager, lifecycle) {
    private val fragmentList: MutableList<Fragment> = fragments

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}