package com.peacedude.lassod_tailor_app.ui

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import com.utsman.recycling.setupAdapter
import kotlinx.android.synthetic.main.fragment_payment_method.*
import kotlinx.android.synthetic.main.fragment_specialty.*
import kotlinx.android.synthetic.main.specialty_layout_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [PaymentMethodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentMethodFragment : Fragment() {
    val title: String by lazy {
        getName()
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
    private lateinit var saveChangesBtn: Button
    private lateinit var saveChangesProgressbar: ProgressBar

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

        })

        payment_method_fab.setOnClickListener {
            dialog.show()
        }
        dialogOk.setOnClickListener {
            payment_method_payment_terms_value_tv.text = GlobalVariables.globalString
            dialog.dismiss()
        }
        dialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        val paymentTermsList = resources.getStringArray(R.array.payment_terms_list).toList().map { str ->
            RecyclerItemForCheckBox(
                str,
                false
            )
        }
        val checkboxes = arrayListOf<CheckBox>()
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

                checkboxes.add(itemView.specialty_item_checkbox)
                checkboxes.forEach {
                    it.setOnCheckedChangeListener { buttonView, isChecked ->
                        when(isChecked){
                            true -> {
                                GlobalVariables.globalString = buttonView.text.toString()
                                checkboxes.forEach{other->
                                    if (other.text != buttonView.text){
                                        other.isChecked = !isChecked
                                    }
                                }
                            }
                        }

                    }
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

    }
}