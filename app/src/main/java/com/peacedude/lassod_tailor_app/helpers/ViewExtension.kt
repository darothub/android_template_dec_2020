package com.peacedude.lassod_tailor_app.helpers

import android.view.View

/**
 * Hide view
 *
 */
fun View.hide(){
    this.visibility = View.GONE
}

/**
 * Show view
 *
 */
fun View.show(){
    this.visibility = View.VISIBLE
}