package com.peacedude.lassod_tailor_app.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.peacedude.lassod_tailor_app.ui.PaymentMethodFragment
import com.peacedude.lassod_tailor_app.ui.ProfileFragment
import com.peacedude.lassod_tailor_app.ui.SpecialtyFragment
import com.peacedude.lassod_tailor_app.ui.UserAccountFragment

class ProfileViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> UserAccountFragment()
            1 -> SpecialtyFragment()
            2 -> PaymentMethodFragment()
            else -> ProfileFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        super.getPageTitle(position)
        return when(position){
            0 -> "Account"
            1 -> "Specialty"
            2 -> "Payment method"
            else -> "NULL"
        }
    }
}