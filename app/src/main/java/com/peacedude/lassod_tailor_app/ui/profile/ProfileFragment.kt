package com.peacedude.lassod_tailor_app.ui.profile

import IsEmptyCheck
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.request.ClientsList
import com.peacedude.lassod_tailor_app.model.request.SingleClient
import com.peacedude.lassod_tailor_app.model.request.User
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.client_list_item.view.*
import kotlinx.android.synthetic.main.fragment_client_account.*
import kotlinx.android.synthetic.main.fragment_email_signup.*
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

    private val loaderDialog by lazy {
        Dialog(requireContext(), R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
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
    private val dialogNameEt by lazy {
        (dialog.findViewById(R.id.single_client_edit_name_et) as TextInputEditText)
    }
    private val dialogPhoneNumberEt by lazy {
        (dialog.findViewById(R.id.single_client_edit_phone_number_et) as TextInputEditText)
    }
    private val dialogEmailEt by lazy {
        (dialog.findViewById(R.id.single_client_edit_email_et) as TextInputEditText)
    }
    private val dialogGenderSpinner by lazy {
        (dialog.findViewById(R.id.single_client_gender_spinner) as Spinner)
    }
    private val dialogAddressEt by lazy {
        (dialog.findViewById(R.id.single_client_edit_delivery_address_et) as TextInputEditText)
    }
    private val dialogStateEt by lazy {
        (dialog.findViewById(R.id.single_client_edit_state_et) as TextInputEditText)
    }

    private val dialogCountrySpinner by lazy {
        (dialog.findViewById(R.id.single_client_edit_country_spinner) as Spinner)
    }
    private val displayClientDialog by lazy {
        (dialog.findViewById(R.id.single_client_display_cl) as ViewGroup)
    }
    private val editClientDialog by lazy {
        (dialog.findViewById(R.id.single_client_edit_cl) as ViewGroup)
    }
    lateinit var  dialogEditBtn:Button
    lateinit var  dialogDeleteBtn:Button
    lateinit var  dialogUpdateBtn:Button
    lateinit var dialogUpdateProgressBar:ProgressBar

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
            //Get included view for delete button
            val includedViewForDeleteBtn = (dialog.findViewById(R.id.single_client_delete_btn) as View)
            //Get included view for edit button
            val includedViewForEditBtn = (dialog.findViewById(R.id.single_client_edit_btn) as View)
            //Get included view for update button
            val includedViewForUpdateBtn = (dialog.findViewById(R.id.single_client_edit_update_btn) as View)
            dialogDeleteBtn = includedViewForDeleteBtn.findViewById(R.id.btn)
            dialogEditBtn = includedViewForEditBtn.findViewById(R.id.btn)
            dialogUpdateBtn = includedViewForUpdateBtn.findViewById(R.id.btn)
            dialogDeleteProgressBar = includedViewForDeleteBtn.findViewById(R.id.progress_bar)
            dialogUpdateProgressBar = includedViewForUpdateBtn.findViewById(R.id.progress_bar)
            dialogDeleteBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            dialogUpdateBtn.setTextColor(
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

            dialogUpdateBtn.background = editBtnBackground

            dialogEditBtn.text = getString(R.string.edit_str)
            dialogDeleteBtn.text = getString(R.string.delete)
            dialogUpdateBtn.text = getString(R.string.update_str)

            setupCategorySpinner(
                requireContext(),
                dialogGenderSpinner,
                R.array.gender_normal_list
            )


        }, {

        })



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
                                displayClientDialog.show()
                                dialog.show{
                                    cornerRadius(10F)
                                }
                            }
                            //When dialog delete button is clicked
                            dialogDeleteBtn.setOnClickListener {
                                val req = authViewModel.deleteClient(header, item?.id)
                                requestObserver(dialogDeleteProgressBar, dialogDeleteBtn, req, true) { bool, result ->
                                    onRequestResponseTask<NothingExpected>(bool, result) {
                                        val res = result as UserResponse<NothingExpected>
                                        requireActivity().gdToast("${res.message}", Gravity.BOTTOM)
//                                        listOfClient.toMutableList().removeAt(position)
                                        adapter.notifyDataSetChanged()
                                        dialog.dismiss()
                                        goto(R.id.profileFragment)

                                    }
                                }

                            }
                            dialogEditBtn.setOnClickListener {
                                loaderDialog.show()
                                if(item != null){
                                    displayClientDialog.hide()
                                    dialogNameEt.setText(item.name)
                                    dialogPhoneNumberEt.setText(item.phone)
                                    dialogEmailEt.setText(item.email)
                                    dialogAddressEt.setText(item.deliveryAddress)
                                    dialogStateEt.setText(item.state)
                                    setUpCountrySpinner(item.country.toString(), dialogCountrySpinner)
                                    editClientDialog.show()
                                    if(editClientDialog.show()){
                                        loaderDialog.dismiss()
                                    }
                                }
                            }
                            dialogUpdateBtn.setOnClickListener {
                                val name = dialogNameEt.text.toString().trim()
                                val phoneNumber = dialogPhoneNumberEt.text.toString()
                                val email = dialogEmailEt.text.toString()
                                val address = dialogAddressEt.text.toString().trim()
                                val state = dialogStateEt.text.toString().trim()
                                val gender = dialogGenderSpinner.selectedItem.toString()
                                val country = dialogCountrySpinner.selectedItem.toString()
                                val emptyEditText =
                                    IsEmptyCheck(dialogNameEt, dialogPhoneNumberEt, dialogAddressEt, dialogStateEt)
//                                val validation = IsEmptyCheck.fieldsValidation(null, null, )
                                when {
                                    emptyEditText != null -> {
                                        emptyEditText.error = getString(R.string.field_required)
                                        requireActivity().gdErrorToast(
                                            "${emptyEditText.tag} is empty",
                                            Gravity.BOTTOM
                                        )
                                    }
                                    else->{
                                        val client = Client(name,phoneNumber, email, address)
                                        client.id = item?.id
                                        client.country = country
                                       val editClientReq = authViewModel.editClient(header, client)
                                        requestObserver(dialogUpdateProgressBar, dialogUpdateBtn, editClientReq){bool, result ->
                                            //Task to be done on successful
                                            onRequestResponseTask<ClientsList>(bool, result) {
                                                val results = result as UserResponse<SingleClient>
                                                val msg = results.message.toString()
                                                val newClientData = result.data?.client
                                                requireActivity().gdToast(msg, Gravity.BOTTOM)
                                                dialog.dismiss()
                                            }

                                        }
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