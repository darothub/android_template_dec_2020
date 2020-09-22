package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peacedude.lassod_tailor_app.R


/**
 * A simple [Fragment] subclass.
 * Use the [ClientAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientAccountFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_account, container, false)
    }

}