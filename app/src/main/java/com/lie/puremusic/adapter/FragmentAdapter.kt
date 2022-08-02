package com.lie.puremusic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    Fragments: MutableList<Fragment?>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var Fragments: MutableList<Fragment?> = ArrayList()

    init {
        this.Fragments = Fragments
    }

    override fun createFragment(position: Int): Fragment {
        return Fragments[position]!!
    }

    override fun getItemCount(): Int {
        return Fragments.size
    }
}