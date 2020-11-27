package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.annotations.SerializedName
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import com.peacedude.lassod_tailor_app.model.request.Client
import com.peacedude.lassod_tailor_app.model.response.*
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.client_list_item.view.*
import kotlinx.android.synthetic.main.fragment_delivery_address.*
import kotlinx.android.synthetic.main.fragment_measurement.*
import kotlinx.android.synthetic.main.measurement_items.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.ArrayList
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [DeliveryAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeliveryAddressFragment : DaggerFragment() {

    private val title by lazy {
        getName()
    }

    val header by lazy {
        authViewModel.header
    }
    private val clientToBeEdited by lazy{
        GlobalVariables.globalClient
    }

    private val dialogAppBar by lazy {
        (dialog.findViewById(R.id.add_delivery_address_layout_dialog_appbar) as AppBarLayout)
    }
    private val dialogToolbar by lazy {
        (dialogAppBar.findViewById(R.id.reusable_appbar_toolbar) as Toolbar)
    }
    private val dialogDeliveryAddressEt by lazy {
        (dialog.findViewById(R.id.add_delivery_address_layout_dialog_et) as TextInputEditText)
    }

    private val dialog by lazy {
        Dialog(requireContext(), R.style.DialogTheme).apply {
            setContentView(R.layout.add_delivery_address_dialog)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(this.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            val window = this.window
            window?.attributes = lp
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCanceledOnTouchOutside(false)
        }
    }
    private val dialogIncludeBtnLayout by lazy {
        (dialog.findViewById(R.id.add_delivery_address_layout_dialog_btn) as View)
    }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    lateinit var addDeliveryaddressBtn: Button
    lateinit var addDeliveryaddressProgressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor(R.color.colorWhite)
        val activityTitle = dialogAppBar.findViewById<TextView>(R.id.reusable_appbar_title_tv)
        activityTitle.text = getString(R.string.add_address_str)
        addDeliveryaddressBtn =
            delivery_address_fragment_add_delivery_address_btn.findViewById(R.id.btn)
        addDeliveryaddressProgressBar = delivery_address_fragment_add_delivery_address_btn.findViewById(R.id.progress_bar)

        val btnBackground = addDeliveryaddressBtn.background
        btnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )
        val dialogAddDeliveryBtn = dialogIncludeBtnLayout.findViewById<Button>(R.id.btn)
        val dialogBtnBackgound = dialogAddDeliveryBtn.background.apply {
            this.changeBackgroundColor(requireContext(), R.color.colorPrimary)
        }


        dialogToolbar.setNavigationOnClickListener {
            dialog.dismiss()
        }



        CoroutineScope(Dispatchers.Main).launch {

            val clientId = clientToBeEdited?.id
            authViewModel.getAllAddress(header, clientId.toString())
                .catch {
                    i(title, "Error on flow ${it.message}")
                }
                .collect {
                    onFlowResponse<DeliveryAddress>(button = dialogAddDeliveryBtn, progressBar = addDeliveryaddressProgressBar, response = it) {
                        i(title, "addresses data flow $it")
                        val listOfAddress = it?.deliveryAddress?.map {address->
                            Client(
                                clientToBeEdited?.name.toString(),
                                clientToBeEdited?.phone.toString(),
                                clientToBeEdited?.email.toString(),
                                address.deliveryAddress
                            ).apply {
                                this.id =  address.clientId
                                this.state
                            }
                        }

                        if(listOfAddress?.isEmpty()!!){
                            delivery_address_fragment_recycler_vf.showNext()
                        }
                        else{
                            delivery_address_fragment_delivery_address_rv.setupAdapter<Client>(R.layout.client_list_item) { adapter, context, list ->

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
                                }
                                setLayoutManager(
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                )
                                submitList(listOfAddress)
                            }
                        }

                    }
                }
        }

        buttonTransactions({
            addDeliveryaddressBtn.apply {
                background = btnBackground
                text = getString(R.string.add_address_str)
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
            }

            dialogAddDeliveryBtn.apply {
                background = dialogBtnBackgound
                text = getString(R.string.save)
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
            }
        }, {

            addDeliveryaddressBtn.setOnClickListener {
                dialog.show()
            }


            Log.i(title, "ClientAccountDeliveryAdd ${clientToBeEdited?.tailorId}")
            if (clientToBeEdited == null) {
                i(title, "No client found")
                btnBackground?.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.colorGray),
                    PorterDuff.Mode.SRC_IN
                )
                addDeliveryaddressBtn.background = btnBackground
                addDeliveryaddressBtn.isClickable = false

            } else {
                dialogAddDeliveryBtn.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        val clientId = clientToBeEdited?.id
                        val address = dialogDeliveryAddressEt.text.toString().trim()
                        authViewModel.addDeliveryAddress(header, clientId, address)
                            .catch {
                                i(title, "Error on flow ${it.message}")
                            }
                            .collect {
                                onFlowResponse<AddressData>(button = dialogAddDeliveryBtn, progressBar = addDeliveryaddressProgressBar, response = it) {
                                    requireActivity().gdToast(getString(R.string.client_address_added_successfully_str), Gravity.BOTTOM)
                                    i(title, "Address data flow $it")
                                    dialog.dismiss()
                                }
                            }
                    }
                }
            }


        })


    }


}