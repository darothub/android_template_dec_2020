package com.peacedude.lassod_tailor_app.ui.profile

import IsEmptyCheck
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
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

    private val loaderDialog by lazy {
        Dialog(requireContext(), R.style.DialogTheme).apply {
            setContentView(R.layout.loader_layout)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
    }

    lateinit var dialogDeleteProgressBar: ProgressBar



    private val dialogNameInitialsTv by lazy {
        (dialog.findViewById(R.id.single_client_name_initials_tv) as TextView)
    }

    private val dialogNameTv by lazy {
        (dialog.findViewById(R.id.single_client_nam_tv) as TextView)
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
    private val exitClientIv by lazy {
        (dialog.findViewById(R.id.single_client_edit_exit_iv) as ImageView)
    }
    private val addMeasurementTv by lazy {
        (dialog.findViewById(R.id.single_client_edit_add_measurement_iv) as TextView)
    }
    private val dialogEmailTv by lazy {
        (dialog.findViewById(R.id.single_client_email_address_tv) as TextView)
    }


    lateinit var  dialogEditBtn:Button
    lateinit var  dialogDeleteBtn:Button
    lateinit var  dialogUpdateBtn:Button
    lateinit var dialogUpdateProgressBar:ProgressBar
    lateinit var dialogSendPhotoBtn:Button
    lateinit var dialogChatBtn:Button

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
        val updateBtnBackground =
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.rounded_corner_background_primary
            )
        val sendPhotoBtnBackground = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.rounded_blue_outline_white_backgrnd
        )


        buttonTransactions({
            //Get included view for delete button
            val includedViewForDeleteBtn =
                (dialog.findViewById(R.id.single_client_deletes_btn) as View)
            //Get included view for edit button
            val includedViewForEditBtn = (dialog.findViewById(R.id.single_client_edits_btn) as View)
            //Get included view for update button
            val includedViewForUpdateBtn =
                (dialog.findViewById(R.id.single_client_edit_update_btn) as View)
            val includedViewForSendPhotoBtn = (dialog.findViewById(R.id.single_client_send_photo_btn) as View)

            val includedViewForChatBtn = (dialog.findViewById(R.id.single_client_chat_btn) as View)
            dialogDeleteBtn = includedViewForDeleteBtn.findViewById(R.id.btn)
            val deleteBtnBackground = dialogDeleteBtn.background
            deleteBtnBackground?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), R.color.colorRed),
                PorterDuff.Mode.SRC_IN
            )

            dialogChatBtn = includedViewForChatBtn.findViewById(R.id.btn)
            val chatBtnBackground = dialogChatBtn.background
            chatBtnBackground?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
            )
            dialogEditBtn = includedViewForEditBtn.findViewById(R.id.btn)
            val editBtnBackground = dialogEditBtn.background
            editBtnBackground?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(requireContext(), R.color.colorGrayDark),
                PorterDuff.Mode.SRC_IN
            )
            dialogUpdateBtn = includedViewForUpdateBtn.findViewById(R.id.btn)
            dialogSendPhotoBtn = includedViewForSendPhotoBtn.findViewById(R.id.btn)
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
                    R.color.colorWhite
                )
            )
            //Set confirm button background
            dialogEditBtn.background = editBtnBackground
            //Set delete button background
            dialogDeleteBtn.background = deleteBtnBackground

            dialogUpdateBtn.background = updateBtnBackground

            dialogChatBtn.background = chatBtnBackground

            dialogSendPhotoBtn.background = sendPhotoBtnBackground

            dialogEditBtn.text = getString(R.string.edit_str)
            dialogDeleteBtn.text = getString(R.string.delete)
            dialogUpdateBtn.text = getString(R.string.update_str)
            dialogChatBtn.text = getString(R.string.chat_str)
            dialogSendPhotoBtn.text = getString(R.string.send_photo_str)
            dialogSendPhotoBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )



            exitClientIv.setOnClickListener {
                dialog.dismiss()
            }

        }, {

            authViewModel.netWorkLiveData.observe(viewLifecycleOwner, Observer {
                if (it) {
                    clientTransaction()
                    Log.i(title, "Profile Network  ON")
                } else {
                    Log.i(title, "Profile Network  OFF")
                }
            })

        })




    }

    @SuppressLint("SetTextI18n")
    private fun clientTransaction() {
        val request = authViewModel.getAllClient(header)
        i(title, "header $header")
        //Observer for get all clients request
        requestObserver(null, null, request, true) { bool, result ->
            //Task to be done on successful
            onRequestResponseTask<ClientsList>(bool, result) {
                val results = result as UserResponse<ClientsList>
                val listOfClient = result.data?.clients?.map {
                    Client(
                        it?.name.toString(),
                        it?.phone.toString(),
                        it?.email.toString(),
                        it?.deliveryAddress.toString()
                    ).apply {
                        country = it?.country
                        state = it?.state
                        tailorId = it?.tailorId
                        id = it?.id
                        gender = it?.gender
                    }


                }

                if (listOfClient?.isNotEmpty()!!) {

                    profile_fragment_client_rv.setupAdapter<Client>(R.layout.client_list_item) { adapter, context, list ->

                        bind { itemView, position, item ->
                            val nameContainsSpace = item?.name?.contains(" ")
                            if(nameContainsSpace!!){
                                val nameSplit = item.name.split(" ")
                                val firstName = nameSplit.get(0)
                                val lastName = nameSplit.get(1)
                                itemView.client_item_name_initials_tv.text = "${firstName.get(0)}${lastName.get(0)}"
                            }
                            else{
                                val firstName = item.name[0]
                                itemView.client_item_name_initials_tv.text = "$firstName"
                            }

                            itemView.client_location_tv.text = item.deliveryAddress
                            itemView.client_name_tv.text = item.name

                            //Set on click listener for item view
                            itemView.setOnClickListener {

                                GlobalVariables.globalId = item?.id.toString()
                                GlobalVariables.globalPosition = position
                                val nameContainsSpace = item.name.contains(" ")
                                if(nameContainsSpace){
                                    val nameSplit = item.name.split(" ")
                                    val firstName = nameSplit.get(0)
                                    val lastName = nameSplit.get(1)
                                    dialogNameInitialsTv.text = "${firstName.get(0)}${lastName.get(0)}"
                                }
                                else{
                                    val firstName = item.name[0]
                                    dialogNameInitialsTv.text = "$firstName"
                                }

//                                itemView.client_location_tv.text = item.deliveryAddress
                                dialogNameTv.text = item.name
//                                dialogNameTv.text = itemView.client_name_tv.text
//                                dialogPhoneNumberTv.text = item?.phone
//                                dialogAddressTv.text = item?.deliveryAddress
//                                dialogGenderTv.text = item?.gender
//                                dialogState.text = item?.state
//                                dialogEmailTv.text = item?.email
//                                dialogCountryTv.text = item?.country
//                                displayClientDialog.show()
                                dialog.show {
                                    cornerRadius(10F)
                                }
                                dialogEditBtn.setOnClickListener {

                                    GlobalVariables.globalClient = item
                                    if (item != null) {

                                        Log.i(title, "Client $item  global ${GlobalVariables.globalClient}")
                                        dialog.dismiss()
                                        startActivity(Intent(requireActivity(), ClientActivity::class.java))
//                                    val lists = resources.getStringArray(R.array.gender_normal_list)
//                                        .toList() as ArrayList<String>
//                                    displayClientDialog.hide()
//                                    dialogNameEt.setText(dialogNameTv.text)
//                                    dialogPhoneNumberEt.setText(dialogPhoneNumberTv.text)
//                                    dialogEmailEt.setText(dialogEmailTv.text)
//                                    dialogAddressEt.setText(dialogAddressTv.text)
//                                    dialogStateEt.setText(dialogState.text)
//                                    setUpSpinnerWithList(
//                                        dialogGenderTv.text.toString(),
//                                        dialogGenderSpinner,
//                                        lists
//                                    )
//                                    setUpCountrySpinner(
//                                        dialogCountryTv.text.toString(),
//                                        dialogCountrySpinner
//                                    )
//                                    editClientDialog.show()
                                        //                                    if(editClientDialog.show()){
                                        //                                        loaderDialog.dismiss()
                                        //                                    }
                                    }
                                }
                            }

                            //When dialog delete button is clicked
                            dialogDeleteBtn.setOnClickListener {
                                val req = authViewModel.deleteClient(
                                    header,
                                    GlobalVariables.globalId
                                )
                                requestObserver(
                                    dialogDeleteProgressBar,
                                    dialogDeleteBtn,
                                    req,
                                    true
                                ) { bool, result ->
                                    onRequestResponseTask<NothingExpected>(bool, result) {
                                        val res = result as UserResponse<NothingExpected>
                                        requireActivity().gdToast("${res.message}", Gravity.BOTTOM)
    //                                        listOfClient.toMutableList().removeAt(position)
                                        list?.removeAt(GlobalVariables.globalPosition)
                                        dialog.dismiss()
                                        adapter.notifyDataSetChanged()

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
                                    IsEmptyCheck(
                                        dialogNameEt,
                                        dialogPhoneNumberEt,
                                        dialogAddressEt,
                                        dialogStateEt
                                    )
    //                                val validation = IsEmptyCheck.fieldsValidation(null, null, )
                                when {
                                    emptyEditText != null -> {
                                        emptyEditText.error = getString(R.string.field_required)
                                        requireActivity().gdErrorToast(
                                            "${emptyEditText.tag} is empty",
                                            Gravity.BOTTOM
                                        )
                                    }
                                    else -> {
                                        item?.apply {
                                            this.name = name
                                            this.phone = phoneNumber
                                            this.email = email
                                            this.deliveryAddress = address
                                            this.gender = gender
                                            this.state = state
                                            this.gender = gender
                                            this.tailorId = GlobalVariables.globalClient?.tailorId
                                            this.country = country
                                        }!!
                                        val client = Client(name, phoneNumber, email, address)
                                        val editClientReq = authViewModel.editClient(header, item)
                                        requestObserver(
                                            dialogUpdateProgressBar,
                                            dialogUpdateBtn,
                                            editClientReq
                                        ) { bool, result ->
                                            //Task to be done on successful
                                            onRequestResponseTask<ClientsList>(bool, result) {
                                                val results = result as UserResponse<SingleClient>
                                                val msg = results.message.toString()

                                                val newClientData = result.data?.client
                                                requireActivity().gdToast(msg, Gravity.BOTTOM)
                                                dialog.dismiss()
                                                adapter.notifyDataSetChanged()

                                            }

                                        }

                                    }
                                }


                            }
                            dialog.setOnDismissListener {
                                displayClientDialog.hide()
                                editClientDialog.hide()
                            }
                            addMeasurementTv.setOnClickListener {
                                GlobalVariables.globalClient = item
                                val measurementIntent = Intent(
                                    requireActivity(),
                                    ClientActivity::class.java
                                )
                                dialog.dismiss()
                                startActivity(measurementIntent)
                            }
                        }
                        setLayoutManager(
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        )
                        submitList(listOfClient)
                    }

                } else {
                    profile_fragment_recyclerview_vf.showNext()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        i(title, "onresume")
    }

}

