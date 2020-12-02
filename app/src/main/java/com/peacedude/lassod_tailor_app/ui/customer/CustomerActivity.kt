package com.peacedude.lassod_tailor_app.ui.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.getName
import com.peacedude.lassod_tailor_app.ui.BaseActivity

class CustomerActivity : BaseActivity() {
    val title: String by lazy {
        getName()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
    }
}