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
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.gdtoast.gdErrorToast
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.*
import com.peacedude.lassod_tailor_app.model.response.NothingExpected
import com.peacedude.lassod_tailor_app.model.response.PhotoList
import com.peacedude.lassod_tailor_app.model.response.SubscriptionResponse
import com.peacedude.lassod_tailor_app.model.response.UserResponse
import com.peacedude.lassod_tailor_app.ui.DashboardActivity
import com.peacedude.lassod_tailor_app.ui.clientmanagement.ClientActivity
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.client_list_item.view.*
import kotlinx.android.synthetic.main.fragment_media.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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


    lateinit var dialogEditBtn:Button
    lateinit var dialogDeleteBtn:Button
    lateinit var dialogUpdateBtn:Button
    lateinit var dialogUpdateProgressBar:ProgressBar
    lateinit var dialogSendPhotoBtn:Button
    lateinit var dialogChatBtn:Button
    lateinit var recyclerView: RecyclerView
    lateinit var viewFlipper: ViewFlipper

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


    @ExperimentalCoroutinesApi
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

        recyclerView = view.findViewById(R.id.profile_fragment_client_rv)
        viewFlipper = view.findViewById(R.id.profile_fragment_recyclerview_vf)

        i(title, "Current user loggedIn ${authViewModel.currentUser?.loggedIn}")

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


        })

        clientTransaction()



        onBackDispatcher {
            i(title, "Back pressed")
            GlobalVariables.globalUser = currentUser
            findNavController().popBackStack()
            finish()
        }


    }

    @ExperimentalCoroutinesApi
    @SuppressLint("SetTextI18n")
    private fun clientTransaction() {
        //Observer for get all clients request
        CoroutineScope(Main).launch {
            supervisorScope {
                val getAllClients = async {
                    authViewModel.getAllClients()
                }

                getAllClients.await()
                    .handleResponse({
                        onFlowResponse<ClientsList>(response = it, error = { err ->
                            i(title, "Error on client getallClicent $err")
                            requireActivity().gdToast(err, Gravity.BOTTOM)
                        }) {result->
                                val list = result?.clients
                                if (list?.isEmpty() == true) {
                                    i(title, "list of clients empty")

                                } else {
                                    i(title, "list of clients not empty")

                                    val listOfClient = list?.map {
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
                                    recyclerView.setupAdapter<Client>(R.layout.client_list_item) { adapter, context, clientList ->

                                        bind { itemView, position, item ->
                                            val nameContainsSpace = item?.name?.contains(" ")
                                            if (nameContainsSpace!!) {
                                                val nameSplit = item.name.split(" ")
                                                val firstName = nameSplit.get(0)
                                                val lastName = nameSplit.get(1)
                                                itemView.client_item_name_initials_tv.text =
                                                    "${firstName.get(0)}${lastName.get(0)}"
                                            } else {
                                                val firstName = item.name[0]
                                                itemView.client_item_name_initials_tv.text = "$firstName"
                                            }

                                            itemView.client_location_tv.text = item.deliveryAddress
                                            itemView.client_name_tv.text = item.name

                                            //Set on click listener for item view
                                            itemView.setOnClickListener {

                                                GlobalVariables.globalId = item?.id.toString()
                                                GlobalVariables.globalPosition = position

                                                if (nameContainsSpace) {
                                                    val nameSplit = item.name.split(" ")
                                                    val firstName = nameSplit.get(0)
                                                    val lastName = nameSplit.get(1)
                                                    dialogNameInitialsTv.text = "${firstName.get(0)}${lastName.get(0)}"
                                                } else {
                                                    val firstName = item.name[0]
                                                    dialogNameInitialsTv.text = "$firstName"
                                                }

                                                dialogNameTv.text = item.name

                                                dialog.show {
                                                    cornerRadius(10F)
                                                }
                                                dialogEditBtn.setOnClickListener {

                                                    GlobalVariables.globalClient = item
                                                    if (item != null) {

                                                        Log.i(
                                                            title,
                                                            "Client $item  global ${GlobalVariables.globalClient}"
                                                        )
                                                        dialog.dismiss()
                                                        startActivity(
                                                            Intent(
                                                                requireActivity(),
                                                                ClientActivity::class.java
                                                            )
                                                        )
                                                    }
                                                }
                                                //When dialog delete button is clicked
                                                dialogDeleteBtn.setOnClickListener {
                                                    dialog.dismiss()

                                                    clientList?.remove(item)
                                                    adapter.notifyDataSetChanged()
                                                    if (clientList?.isEmpty()!!) {
                                                        profile_fragment_recyclerview_vf.showPrevious()
                                                    }
                                                    val req = authViewModel.deleteClient(
                                                        header,
                                                        item.id
                                                    )

                                                    observeRequest<NothingExpected>(
                                                        req,
                                                        dialogDeleteProgressBar,
                                                        dialogDeleteBtn,
                                                        false,
                                                        { result ->
                                                            val res = result
                                                            requireActivity().gdToast(
                                                                "${res.message}",
                                                                Gravity.BOTTOM
                                                            )

                                                            i(title, "We are here ")


                                                        },
                                                        { err ->
                                                            clientList.add(item)
                                                            adapter.notifyDataSetChanged()
                                                            requireActivity().gdErrorToast(
                                                                err,
                                                                Gravity.BOTTOM
                                                            )
                                                            i(title, "DeleteClientError $err")
                                                        })

                                                }
                                            }




                                            dialog.setOnDismissListener {
                                                displayClientDialog.hide()
                                                editClientDialog.hide()
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
                                        viewFlipper.showNext()
                                    }

                                }


//                            requireActivity().gdToast(it?.message.toString(), Gravity.BOTTOM)

                        }
                    }, {err ->
                        requireActivity().gdToast(err, Gravity.BOTTOM)
                    })
            }
        }

    }

    override fun onResume() {
        super.onResume()

        i(title, "onresume")
    }

    override fun onPause() {
        super.onPause()
        val c = GlobalVariables.globalClientList
        i(title, "CforClient $c")
    }

}

