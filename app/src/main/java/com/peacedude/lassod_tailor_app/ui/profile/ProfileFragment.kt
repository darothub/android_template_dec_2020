package com.peacedude.lassod_tailor_app.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.DashboardActivity
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.added_client_list_item.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_resources.*
import kotlinx.android.synthetic.main.resource_video_item.view.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : DaggerFragment() {
    private val title by lazy {
        getName()
    }
    //Get logged-in user
    private val currentUser: User? by lazy {
        authViewModel.currentUser
    }

    private val header by lazy {
        authViewModel.header
    }
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val request = authViewModel.getAllClient(header, currentUser?.id)
        i(title, "header ${currentUser?.id}")
        requireActivity().requestObserver(
            null,
            null,
            request
        ) { bool, result ->
            onRequestResponseTask<List<Client>>(bool, result) {
                val results = result as UserResponse<List<Client>>
                val listOfClient = result.data

                if (listOfClient.isNotEmpty()){
                    no_client_included_layout.hide()
                }
                else{
                    profile_fragment_client_rv.show()
                    profile_fragment_client_rv.setupAdapter<Client>(R.layout.added_client_list_item) { adapter, context, list ->
                        bind { itemView, position, item ->
                            itemView.client_name_tv.text = item?.name
                            itemView.client_location_tv.text = item?.country
                        }
                        setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false))
                        submitList(listOfClient)
                    }
                }
            }
        }

    }

}