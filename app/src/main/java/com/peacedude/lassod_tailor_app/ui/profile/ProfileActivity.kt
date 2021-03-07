package com.peacedude.lassod_tailor_app.ui.profile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar = profile_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)
        val activityTitle = profile_activity_appbar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.edit_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
