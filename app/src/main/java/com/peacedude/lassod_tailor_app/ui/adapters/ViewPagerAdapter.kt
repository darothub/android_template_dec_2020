package com.peacedude.lassod_tailor_app.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentActivity, private val size:Int, private val fragments:(Int)->Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments(position)
    }

}