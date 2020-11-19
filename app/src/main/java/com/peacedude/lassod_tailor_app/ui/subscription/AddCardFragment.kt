package com.peacedude.lassod_tailor_app.ui.subscription

import IsEmptyCheck
import android.app.Dialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import co.paystack.android.Paystack.TransactionCallback
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.peacedude.gdtoast.gdToast
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.helpers.*
import kotlinx.android.synthetic.main.activity_subscription.*
import kotlinx.android.synthetic.main.fragment_add_card.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCardFragment : Fragment() {

    val title by lazy {
        getName()
    }

    val cardDetailIncludedBtnLayout by lazy {
        add_card_fragment_card_detail_layout.findViewById<View>(R.id.card_details_include_btn)
    }

    val cardDetailIncludedCardNumberEt by lazy {
        add_card_fragment_card_detail_layout.findViewById<TextInputEditText>(R.id.card_details_card_number_et)
    }
    val cardDetailIncludedCVVEt by lazy {
        add_card_fragment_card_detail_layout.findViewById<EditText>(R.id.card_details_card_cvv_et)
    }

    val cardDetailIncludedMonthEt by lazy {
        add_card_fragment_card_detail_layout.findViewById<EditText>(R.id.card_details_card_month_et)
    }

    private val dialog by lazy {
        Dialog(requireContext(), R.style.DialogTheme).apply {
            setContentView(R.layout.success_dialog_layout)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
    lateinit var button: Button
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
        return inflater.inflate(R.layout.fragment_add_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val payBtnBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_background)
        payBtnBackground?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorGreen),
            PorterDuff.Mode.SRC_IN
        )
        cardDetailIncludedMonthEt.doOnTextChanged { text, start, before, count ->
            val expiryMonthYearPattern = Regex("""^(?!.*1[3-9])[0-1][0-9]/2[0-9]$""")
            when{

                text?.length == 2 && text?.length < 3->{
                    val t = "$text/"
                    cardDetailIncludedMonthEt.setText(t)
                    cardDetailIncludedMonthEt.setSelection(cardDetailIncludedMonthEt.text.length )

                }
                !expiryMonthYearPattern.matches(text.toString()) -> {
                    cardDetailIncludedMonthEt.error = getString(R.string.invalid_expiry_date_str)
                }
            }
        }
        cardDetailIncludedCardNumberEt.doOnTextChanged { text, start, before, count ->

            when{

                text?.length == 4 ->{
                    val t = "$text "
                    cardDetailIncludedCardNumberEt.setText(t)
                    cardDetailIncludedCardNumberEt.text?.length?.let {
                        cardDetailIncludedCardNumberEt.setSelection(
                            it
                        )
                    }

                }
            }
        }

        buttonTransactions({
            button = cardDetailIncludedBtnLayout.findViewById(R.id.btn)
            button.background = payBtnBackground
            button.text = getString(R.string.pay)
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorWhite
                )
            )
        }, {
            button.setOnClickListener {
                val checkforEmptyEt = IsEmptyCheck(
                    cardDetailIncludedCardNumberEt,
                    cardDetailIncludedCVVEt,
                    cardDetailIncludedMonthEt
                )
                val expiryMonthYearPattern = Regex("""^(?!.*1[3-9])[0-1][0-9]/2[0-9]$""")
                val expiryDate = cardDetailIncludedMonthEt.text.toString()
                val matches = expiryMonthYearPattern.matches(expiryDate)
                val cvvPattern = Regex("""\d{3}""")
                val cvv = cardDetailIncludedCVVEt.text.toString()
                val cvvMatches = cvvPattern.matches(cvv)
                when {
                    checkforEmptyEt != null -> {
                        checkforEmptyEt.error = "${checkforEmptyEt.tag} is empty"
                        requireActivity().gdToast("${checkforEmptyEt.tag} is empty", Gravity.BOTTOM)
                    }
                    matches == false -> {
                        cardDetailIncludedMonthEt.error =
                            getString(R.string.invalid_expiry_date_str)
                        requireActivity().gdToast(
                            getString(R.string.invalid_expiry_date_str),
                            Gravity.BOTTOM
                        )
                    }
                    cvvMatches == false -> {
                        cardDetailIncludedCVVEt.error = getString(R.string.invalid_cvv_str)
                        requireActivity().gdToast(
                            getString(R.string.invalid_cvv_str),
                            Gravity.BOTTOM
                        )
                    }
                    else -> {
                        val number = cardDetailIncludedCardNumberEt.text.toString()
                        val date = cardDetailIncludedMonthEt.text.toString()
                        val dateSplit = date.split("/")
                        val month = dateSplit[0].toInt()
                        val year = dateSplit[1].toInt()
                        val card = Card(number, month, year, cvv)
                        val charge = Charge()
                        charge.card = card
                        PaystackSdk.chargeCard(
                            requireActivity(),
                            charge,
                            object : TransactionCallback {
                                override fun onSuccess(transaction: Transaction?) {
                                    // This is called only after transaction is deemed successful.
                                    // Retrieve the transaction, and send its reference to your server
                                    // for verification.
                                    i(title, "OnSuccess $transaction")
                                }

                                override fun beforeValidate(transaction: Transaction?) {
                                    // This is called only before requesting OTP.
                                    // Save reference so you may send to server. If
                                    // error occurs with OTP, you should still verify on server.

                                    i(title, "BeforeValidate ${transaction?.reference}")
                                }

                                override fun onError(
                                    error: Throwable?,
                                    transaction: Transaction?
                                ) {
                                    //handle error here
                                    i(title, "OnError $error")
                                }
                            })
                    }
                }
            }
        })
        add_card_fragment_add_card_layout.setOnClickListener {
            add_fragment_vf.showNext()
        }
        (requireActivity().subscription_activity_appbar.findViewById<Toolbar>(R.id.reusable_appbar_toolbar)).setNavigationOnClickListener {
            if (add_card_fragment_add_card_layout.hide()) {
                add_card_fragment_add_card_layout.show()
                add_card_fragment_card_detail_layout.hide()
            } else {
                add_card_fragment_add_card_layout.show()
//                requireActivity().gdToast("Here", Gravity.BOTTOM)
                requireActivity().finish()
            }
        }

//        cardDetailIncludedMonthEt.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                val calendar = Calendar.getInstance()
//                val defaultYear = calendar.get(Calendar.YEAR)
//                val defaultMonth = calendar.get(Calendar.MONTH)
//                val dialogFragment =
//                    MonthYearPickerDialogFragment.getInstance(defaultMonth, defaultYear)
//                dialogFragment.show(requireActivity().supportFragmentManager, null)
//
//                dialogFragment.setOnDateSetListener { year, monthOfYear ->
//                    i(title, "Year $year, Month $monthOfYear")
//                }
//            }
//
//        }


    }

    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }

}