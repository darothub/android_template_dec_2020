package com.peacedude.lassod_tailor_app.ui.profile

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.NothingSpoil
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.client_list_item.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
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

    lateinit var dialogDeleteProgressBar: ProgressBar

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
        i(title, "onviewcreated")
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
            dialogDeleteProgressBar = includedViewForDeleteBtn.findViewById(R.id.progress_bar)
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
        //Observer for get all clients request
        requestObserver(null, null, request) { bool, result ->
            //Task to be done on successful
            onRequestResponseTask<ClientsList>(bool, result) {
                val results = result as UserResponse<ClientsList>
                val listOfClient = result.data?.clients

                if (listOfClient?.isNotEmpty()!!){
                    no_client_included_layout.hide()
                    profile_fragment_client_rv.show()
                    profile_fragment_client_rv.setupAdapter<Client>(R.layout.client_list_item) { adapter, context, list ->

                        bind { itemView, position, item ->
                            itemView.client_name_tv.text = item?.name
                            itemView.client_location_tv.text = "Nigeria"

                            //Set on click listener for item view
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
                            //When dialog delete button is clicked
                            dialogDeleteBtn.setOnClickListener {
                                val req = authViewModel.deleteClient(header, item?.id)
                                requestObserver(dialogDeleteProgressBar, dialogDeleteBtn, req, true) { bool, result ->
                                    onRequestResponseTask<NothingSpoil>(bool, result) {
                                        val res = result as UserResponse<NothingSpoil>
                                        requireActivity().gdToast("${res.message}", Gravity.BOTTOM)
//                                        listOfClient.toMutableList().removeAt(position)
                                        adapter.notifyDataSetChanged()
                                        dialog.dismiss()
                                        goto(R.id.profileFragment)

                                    }
                                }

                            }
                        }
                        setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false))
                        submitList(listOfClient)
                    }

                }
                else{
                    no_client_included_layout.show()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        i(title, "onresume")
    }

}