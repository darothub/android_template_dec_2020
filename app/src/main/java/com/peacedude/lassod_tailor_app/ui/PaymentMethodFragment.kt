package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.data.viewmodel.auth.AuthViewModel
import com.peacedude.lassod_tailor_app.data.viewmodel.factory.ViewModelFactory
import com.peacedude.lassod_tailor_app.helpers.*
import com.peacedude.lassod_tailor_app.model.request.User
import com.utsman.recycling.setupAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_payment_method.*
import kotlinx.android.synthetic.main.fragment_specialty.*
import kotlinx.android.synthetic.main.specialty_layout_item.view.*
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [PaymentMethodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentMethodFragment : DaggerFragment() {
    val title: String by lazy {
        getName()
    }
    //Get logged-in user
    private val currentUser: User? by lazy {
        authViewModel.currentUser
    }
    val header by lazy {
        authViewModel.header
    }
    val dialog by lazy {
        MaterialDialog(requireContext()).apply {
            noAutoDismiss()
            customView(R.layout.payment_terms_dialog_layout)
        }
    }

    val dialogRv by lazy {
        (dialog.findViewById<RecyclerView>(R.id.payment_terms_dialog_rv))
    }
    val dialogOk by lazy {
        (dialog.findViewById<TextView>(R.id.dialog_ok_tv))
    }
    val dialogCancel by lazy {
        (dialog.findViewById<TextView>(R.id.dialog_cancel_tv))
    }
    @Inject
    lateinit var viewModelProviderFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactory).get(AuthViewModel::class.java)
    }
    private lateinit var saveChangesBtn: Button
    private lateinit var saveChangesProgressbar: ProgressBar
    var checkBoxStringForPaymentTerm = ArrayList<String>()
    var paymentTermString = ""
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
        return inflater.inflate(R.layout.fragment_payment_method, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonTransactions({
            saveChangesBtn = payment_method_save_btn_include.findViewById(R.id.btn)
            saveChangesProgressbar = payment_method_save_btn_include.findViewById(R.id.progress_bar)
            val saveChangesBtnBackground = saveChangesBtn.background
            saveChangesBtnBackground.changeBackgroundColor(requireContext(), R.color.colorPrimary)
            saveChangesBtn.background = saveChangesBtnBackground
            saveChangesBtn.text = getString(R.string.save_changes)
        },{

            var y:ArrayList<Any> = arrayListOf(1, "2", 3)

        })

        var n:User? = null

//        CoroutineScope(Dispatchers.Main).launch {
//            supervisorScope {
//                val getUserCall = async { authViewModel.getUserDetails(header.toString()) }
//                try {
//                    getUserCall.await()
//                        .handleResponse({
//                            onFlowResponse<User>(response = it) { user ->
//                                i(title, "paymentMethod ${user?.paymentTerms}")
//
//                                when{
//                                    user?.paymentTerms != null -> {
//                                        var pTerms = ""
//                                        user.paymentTerms?.forEach {str->
//                                            pTerms +="$str\n\n"
//                                            payment_method_payment_terms_value_tv.text = pTerms
//                                        }
//                                    }
//                                }
//                                when{
//                                    user?.paymentOptions != null -> {
//                                        var pOptions = ""
//                                        user.paymentOptions?.forEach { st ->
//                                            pOptions += "$st\n\n"
//                                            payment_method_payment_options_value_tv.text = pOptions
//                                        }
//                                    }
//                                }
//
//                                n = user
//                                var newUser = user
//
//                                payment_method_fab.setOnClickListener {
//                                    checkBoxStringForPaymentTerm.clear()
//                                    setPaymentTermsValue(newUser)
//                                }
//
//                                payment_options_fab.setOnClickListener {
//                                    setPaymentOptionsValue(newUser)
//                                }
//                                saveChangesBtn.setOnClickListener {
//                                    updateUserData(newUser)
//                                }
//                            }
//                        }) { err ->
//                            i(title, "Caught error $err")
//                        }
//
//
//                } catch (e: Exception) {
//                    i(title, "Get user details error data flow ${e.message}")
//                }
//            }
//        }


    }

    private fun setPaymentOptionsValue(user: User?) {
        val paymentOptionsList =
            resources.getStringArray(R.array.payment_options_list).toList().map { str ->
                if (user?.paymentOptions != null && user.paymentOptions?.contains(str)!!) {
                    RecyclerItemForCheckBox(text = str, selected = true)
                } else {
                    RecyclerItemForCheckBox(text = str)
                }
            }
        val checkboxesText = arrayListOf<String>()
        dialogRv.setupAdapter<RecyclerItemForCheckBox>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Dispatchers.Main).launch {
                    itemView.specialty_item_checkbox.show()
                    itemView.specialty_item_checkbox.text = item?.text
                    itemView.specialty_item_checkbox.isChecked = item?.selected!!
                    delay(200)
                    itemView.shimmerLayout.stopShimmer()
                    itemView.shimmerLayout.setShimmer(null)
                    itemView.item_container.background = null
                }

                itemView.specialty_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->

                    when (isChecked) {
                        true -> {
                            item?.selected = isChecked

                        }
                        false -> {
                            item?.selected = isChecked

                        }
                    }

                    adapter.notifyDataSetChanged()
                }


                dialogOk.setOnClickListener {
                    val selectedOptions = paymentOptionsList.filter { it.selected }.map { it.text }
                    user?.paymentOptions = selectedOptions
                    payment_method_payment_options_value_tv.text = selectedOptions.joinToString( "\n\n")
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

            }
            setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )

            submitList(paymentOptionsList)
        }
        dialog.show()
    }

    private fun setPaymentTermsValue(user: User?) {
        dialogOk.setOnClickListener {
            payment_method_payment_terms_value_tv.text = GlobalVariables.globalString
            GlobalVariables.globalString = ""
            dialog.dismiss()
        }
        dialogCancel.setOnClickListener {
            dialog.dismiss()
        }
        val paymentTermsList =
            resources.getStringArray(R.array.payment_terms_list).toList().map { str ->
                if (user?.paymentTerms != null && user.paymentTerms?.contains(str)!!) {
                    RecyclerItemForCheckBox(text = str, selected = true)
                } else {
                    RecyclerItemForCheckBox(text = str)
                }
            }
        dialogRv.setupAdapter<RecyclerItemForCheckBox>(R.layout.specialty_layout_item) { adapter, context, list ->
            bind { itemView, position, item ->
                CoroutineScope(Dispatchers.Main).launch {
                    itemView.specialty_item_checkbox.show()
                    itemView.specialty_item_checkbox.text = item?.text
                    itemView.specialty_item_checkbox.isChecked = item?.selected!!
                    delay(200)
                    itemView.shimmerLayout.stopShimmer()
                    itemView.shimmerLayout.setShimmer(null)
                    itemView.item_container.background = null
                }


                itemView.specialty_item_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                    when (isChecked) {
                        true -> {
                            checkBoxStringForPaymentTerm.add("${buttonView.text}")
                            i(title, " check $isChecked list $checkBoxStringForPaymentTerm")
                            item?.selected = isChecked

                        }
                        false ->{
                            checkBoxStringForPaymentTerm.remove("${buttonView.text}")
                            item?.selected = isChecked
                            i(title, " check $isChecked list $checkBoxStringForPaymentTerm")

                        }
                    }
                    adapter.notifyDataSetChanged()

                }
                dialogOk.setOnClickListener {
                    val selectedTerms = paymentTermsList.filter { it.selected }.map { it.text }
                    payment_method_payment_terms_value_tv.text = selectedTerms.joinToString( "\n\n")
//                        val pTerms = paymentTermString.split("\n\n").map{str -> str.replace("*", "")}.dropLast(1)
                    user?.paymentTerms = selectedTerms
                    adapter.notifyDataSetChanged()
                    i("PaymentTerms", "$selectedTerms")
                    dialog.dismiss()
                }

            }
            setLayoutManager(
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )
            submitList(paymentTermsList)
        }

        dialog.show()
    }

    private fun updateUserData(user: User?) {
        user?.isVerified = true
        val request = user?.let { authViewModel.updateUserData(it) }
        observeRequest<User>(request, saveChangesProgressbar, saveChangesBtn, false, {response ->
            val msg = response.message
            val profileData = response.data
            authViewModel.profileData = profileData
            currentUser?.paymentOptions = user?.paymentOptions
            currentUser?.paymentTerms = user?.paymentTerms
            authViewModel.currentUser = currentUser!!
            i(title, "Update user ${user?.paymentTerms}")
            requireActivity().gdToast(msg.toString(), Gravity.BOTTOM)
        },{err->
            i(title, "PaymentMethodError $err")
        })
//        response.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            val (bool, result) = it
//            onRequestResponseTask<User>(bool, result) {
//                val response = result as? UserResponse<User>
//                val msg = response?.message
//                val profileData = response?.data
//                authViewModel.profileData = profileData
//                currentUser?.paymentOptions = user?.paymentOptions
//                currentUser?.paymentTerms = user?.paymentTerms
//                authViewModel.currentUser = currentUser
//                i(title, "Update user ${user?.paymentTerms}")
//                requireActivity().gdToast(msg.toString(), Gravity.BOTTOM)
//            }
//        })
    }
}