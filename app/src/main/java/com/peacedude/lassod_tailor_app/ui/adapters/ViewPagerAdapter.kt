package com.peacedude.lassod_tailor_app.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.peacedude.lassod_tailor_app.ui.PaymentMethodFragment
import com.peacedude.lassod_tailor_app.ui.ProfileFragment
import com.peacedude.lassod_tailor_app.ui.SpecialtyFragment
import com.peacedude.lassod_tailor_app.ui.UserAccountFragment

class ViewPagerAdapter(fm: FragmentActivity, private val size:Int, private val fragments:(Int)->Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments(position)
    }

}