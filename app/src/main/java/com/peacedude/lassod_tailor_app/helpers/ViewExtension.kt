package com.peacedude.lassod_tailor_app.helpers

import android.view.View

/**
 * Hide view
 *
 */
fun View.hide():Boolean{
    if(this.visibility == View.VISIBLE || this.visibility == View.INVISIBLE){
        this.visibility = View.GONE
        return false
    }
    return this.visibility == View.GONE
}

/**
 * Show view
 *
 */
fun View.show():Boolean{
    if(this.visibility == View.INVISIBLE || this.visibility == View.GONE){
        this.visibility = View.VISIBLE
        return false
    }
    return this.visibility == View.VISIBLE
}
/**
 * Invisible view
 *
 */
fun View.invisible():Boolean{
    if(this.visibility == View.VISIBLE || this.visibility == View.GONE){
        this.visibility = View.INVISIBLE
        return false
    }
    return this.visibility == View.INVISIBLE
}