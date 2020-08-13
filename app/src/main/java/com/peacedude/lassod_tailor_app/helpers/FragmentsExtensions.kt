package com.peacedude.lassod_tailor_app.helpers

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.goto(destinationId: Int) {
    findNavController().navigate(destinationId)
}