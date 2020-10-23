package com.peacedude.lassod_tailor_app.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.data.viewmodel.user.UserViewModel
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.ResourcesVideo
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.NothingSpoil
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.DashboardActivity
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.added_client_list_item.view.*
import kotlinx.android.synthetic.main.fragment_phone_signup.*
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

    private val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.single_client_layout)
        }
    }

    private val dialogNameTv by lazy {
        (dialog.findViewById(R.id.single_client_name_tv) as TextView)
    }
    private val dialogPhoneNumberTv by lazy {
        (dialog.findViewById(R.id.single_client_phone_number_tv) as TextView)
    }
    private val dialogGenderTv by lazy {
        (dialog.findViewById(R.id.single_client_gender_tv) as TextView)
    }
    private val dialogAddressTv by lazy {
        (dialog.findViewById(R.id.single_client_delivery_address_tv) as TextView)
    }
    private val dialogState by lazy {
        (dialog.findViewById(R.id.single_client_state_tv) as TextView)
    }
    private val dialogCountryTv by lazy {
        (dialog.findViewById(R.id.single_client_country_tv) as TextView)
    }
    lateinit var  dialogEditBtn:Button
    lateinit var  dialogDeleteBtn:Button
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
        //Drawable background for resend code button
        val editBtnBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background_primary)
        val deleteBtnBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner__red_background)
        buttonTransactions({
            //Get included view for confirm button
            val includedViewForDeleteBtn = (dialog.findViewById(R.id.single_client_delete_btn) as View)
            //Get included view for resend code button
            val includedViewForEditBtn = (dialog.findViewById(R.id.single_client_edit_btn) as View)
            dialogDeleteBtn = includedViewForDeleteBtn.findViewById(R.id.btn)
            dialogEditBtn = includedViewForEditBtn.findViewById(R.id.btn)
            dialogDeleteBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            dialogEditBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            //Set confirm button background
            dialogEditBtn.background = editBtnBackground
            //Set delete button background
            dialogDeleteBtn.background = deleteBtnBackground

            dialogEditBtn.text = getString(R.string.edit_str)
            dialogDeleteBtn.text = getString(R.string.delete)



        }, {})




        val request = authViewModel.getAllClient(header)
        i(title, "header $header")
        requireActivity().requestObserver(
            null,
            null,
            request
        ) { bool, result ->
            onRequestResponseTask<ClientsList>(bool, result) {
                val results = result as UserResponse<ClientsList>
                val listOfClient = result.data.clients

                if (listOfClient.isNotEmpty()){
                    no_client_included_layout.hide()
                    profile_fragment_client_rv.show()
                    profile_fragment_client_rv.setupAdapter<Client>(R.layout.added_client_list_item) { adapter, context, list ->

                        bind { itemView, position, item ->
                            itemView.client_name_tv.text = item?.name
                            itemView.client_location_tv.text = "Nigeria"


                            itemView.setOnClickListener {
                                dialogNameTv.text = itemView.client_name_tv.text
                                dialogPhoneNumberTv.text = item?.phone
                                dialogAddressTv.text = item?.deliveryAddress
                                dialogGenderTv.text = item?.gender
                                dialogState.text = "Lasgidi"
                                dialogCountryTv.text = "Nigeria"
                                dialog.show{
                                    cornerRadius(10F)
                                }
                            }
                        }
                        setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false))
                        submitList(listOfClient)
                    }

                    //Swipe listener for left swipe(to delete)
//                    ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
//                        override fun onMove(
//                            recyclerView: RecyclerView,
//                            viewHolder: RecyclerView.ViewHolder,
//                            target: RecyclerView.ViewHolder
//                        ): Boolean {
//                            return false
//                        }
//                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                            val req = authViewModel.deleteClient(header, item?.id)
//                            requireActivity().requestObserver(
//                                null,
//                                null,
//                                req
//                            ) { bool, result ->
//                                onRequestResponseTask<NothingSpoil>(bool, result) {
//
//                                }
//                            }
//                        }
//
//                    }).attachToRecyclerView(profile_fragment_client_rv)

                }
                else{
                    no_client_included_layout.show()
                }
            }
        }

    }

}