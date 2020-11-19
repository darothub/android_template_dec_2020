package com.peacedude.lassod_tailor_app.ui.clientmanagement

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.response.AddressData
import com.peacedude.lassod_tailor_app.model.response.Form
import com.peacedude.lassod_tailor_app.model.response.MeasurementTypeList
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.fragment_delivery_address.*
import kotlinx.android.synthetic.main.fragment_measurement.*
import kotlinx.android.synthetic.main.measurement_items.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
    private val toolbar by lazy {
        resources_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)
    }

    val header by lazy {
        authViewModel.header
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
        addDeliveryaddressBtn = delivery_address_fragment_add_delivery_address_btn.findViewById(R.id.btn)

        val btnBackground = addDeliveryaddressBtn.background
        btnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )
        val dialogAddDeliveryBtn = dialogIncludeBtnLayout.findViewById<Button>(R.id.btn)
        val dialogBtnBackgound = dialogAddDeliveryBtn.background
        dialogBtnBackgound?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        dialogToolbar.setNavigationOnClickListener {
            dialog.dismiss()
        }

        buttonTransactions({
            addDeliveryaddressBtn.background = btnBackground
            addDeliveryaddressBtn.text = getString(R.string.add_address_str)
            addDeliveryaddressBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
            dialogAddDeliveryBtn.background = dialogBtnBackgound
            dialogAddDeliveryBtn.text = getString(R.string.save)
            dialogAddDeliveryBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorAccent
                )
            )
        }, {

            addDeliveryaddressBtn.setOnClickListener {
                dialog.show()
            }

            dialogAddDeliveryBtn.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val clientId = GlobalVariables.globalId
                    val address = dialogDeliveryAddressEt.text.toString().trim()
                    authViewModel.addDeliveryAddress(header, clientId, address)
                        .catch {
                            i(title, "Error on flow ${it.message}")
                        }
                        .collect {
                            onFlowResponse<AddressData>(response = it) {
                                i(title, "Address data flow ${it?.address}")
                            }
                        }

                }
            }

        })


    }


}